package levelup.desktop.java.ui.lembretes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LembreteService {

    public static class Lembrete {
        private final String titulo;
        private final LocalTime horario;
        private boolean disparado;

        public Lembrete(String titulo, LocalTime horario) {
            this.titulo = titulo;
            this.horario = horario;
        }

        public String getTitulo() {
            return titulo;
        }

        public LocalTime getHorario() {
            return horario;
        }

        public boolean isDisparado() {
            return disparado;
        }

        public void setDisparado(boolean disparado) {
            this.disparado = disparado;
        }
    }

    private final VBox formNovoLembrete;
    private final TextField campoTituloLembrete;
    private final TextField campoHorarioLembrete;
    private final VBox listaLembretes;
    private final Button btnSalvarLembrete;
    private final Button btnSomLembretes;
    private final ImageView iconeSomLembretes;

    private final Image iconeDelete;
    private final Image iconeSomAtivo;
    private final Image iconeSomDesativado;

    private Label lblSemLembretes;

    private final Consumer<Boolean> callbackSomTrocado;

    private final List<Lembrete> lembretes = new ArrayList<>();
    private boolean somAtivo = true;

    private final DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

    public LembreteService(
            VBox formNovoLembrete,
            TextField campoTituloLembrete,
            TextField campoHorarioLembrete,
            VBox listaLembretes,
            Button btnSalvarLembrete,
            Button btnSomLembretes,
            ImageView iconeSomLembretes,
            Image iconeDelete,
            Image iconeSomAtivo,
            Image iconeSomDesativado,
            Consumer<Boolean> callbackSomTrocado) {
        this.formNovoLembrete = formNovoLembrete;
        this.campoTituloLembrete = campoTituloLembrete;
        this.campoHorarioLembrete = campoHorarioLembrete;
        this.listaLembretes = listaLembretes;
        this.btnSalvarLembrete = btnSalvarLembrete;
        this.btnSomLembretes = btnSomLembretes;
        this.iconeSomLembretes = iconeSomLembretes;
        this.iconeDelete = iconeDelete;
        this.iconeSomAtivo = iconeSomAtivo;
        this.iconeSomDesativado = iconeSomDesativado;
        this.callbackSomTrocado = callbackSomTrocado;

        if (formNovoLembrete != null && formNovoLembrete.getScene() != null) {
            var node = formNovoLembrete.getScene().lookup("#lblSemLembretes");
            if (node instanceof Label lbl) {
                lblSemLembretes = lbl;
            }
        }

        configurarBotoes();
        atualizarIconeSom();
        atualizarListaVisual();
    }

    private void configurarBotoes() {
        if (btnSalvarLembrete != null) {
            btnSalvarLembrete.setOnAction(e -> salvarNovoLembrete());
        }

        if (btnSomLembretes != null) {
            btnSomLembretes.setOnAction(e -> alternarSom());
        }
    }

    private void atualizarIconeSom() {
        if (iconeSomLembretes != null) {
            Image img = somAtivo ? iconeSomAtivo : iconeSomDesativado;
            if (img != null) {
                iconeSomLembretes.setImage(img);
            }
        }
    }

    private void alternarSom() {
        somAtivo = !somAtivo;
        atualizarIconeSom();
        if (callbackSomTrocado != null) {
            callbackSomTrocado.accept(somAtivo);
        }
    }

    public boolean isSomAtivo() {
        return somAtivo;
    }

    public void alternarFormulario() {
        if (formNovoLembrete == null)
            return;
        boolean novoVisivel = !formNovoLembrete.isVisible();
        formNovoLembrete.setVisible(novoVisivel);
        formNovoLembrete.setManaged(novoVisivel);
    }

    private void salvarNovoLembrete() {
        if (campoTituloLembrete == null || campoHorarioLembrete == null)
            return;

        String titulo = campoTituloLembrete.getText() != null
                ? campoTituloLembrete.getText().trim()
                : "";
        String horarioStr = campoHorarioLembrete.getText() != null
                ? campoHorarioLembrete.getText().trim()
                : "";

        if (titulo.isEmpty() || horarioStr.isEmpty()) {

            return;
        }

        LocalTime horario;
        try {
            horario = LocalTime.parse(horarioStr, formatterHora);
        } catch (DateTimeParseException e) {
            System.out.println("[LEMBRETES] Horário inválido: " + horarioStr + " (use HH:mm)");
            return;
        }

        Lembrete lembrete = new Lembrete(titulo, horario);
        lembretes.add(lembrete);

        campoTituloLembrete.clear();
        campoHorarioLembrete.clear();

        if (formNovoLembrete != null) {
            formNovoLembrete.setVisible(false);
            formNovoLembrete.setManaged(false);
        }

        atualizarListaVisual();
    }

    private void atualizarListaVisual() {
        if (listaLembretes == null)
            return;

        listaLembretes.getChildren().clear();

        if (lblSemLembretes != null) {
            boolean semItens = lembretes.isEmpty();
            lblSemLembretes.setVisible(semItens);
            lblSemLembretes.setManaged(semItens);
        }

        for (Lembrete lembrete : lembretes) {
            HBox linha = criarLinhaLembrete(lembrete);
            listaLembretes.getChildren().add(linha);
        }
    }

    private HBox criarLinhaLembrete(Lembrete lembrete) {
        HBox linha = new HBox(8);
        linha.getStyleClass().add("lembrete-item");

        String texto = String.format("%s  %s",
                lembrete.getHorario().format(formatterHora),
                lembrete.getTitulo());

        Label lblTexto = new Label(texto);
        lblTexto.getStyleClass().add("lembrete-item-text");

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        Button btnExcluir = new Button();
        btnExcluir.getStyleClass().add("lembrete-delete-btn");

        if (iconeDelete != null) {
            ImageView iv = new ImageView(iconeDelete);
            iv.setFitWidth(14);
            iv.setFitHeight(14);
            iv.setPreserveRatio(true);
            btnExcluir.setGraphic(iv);
        }

        btnExcluir.setOnAction(e -> removerLembrete(lembrete));

        linha.getChildren().addAll(lblTexto, espaco, btnExcluir);
        return linha;
    }

    public List<Lembrete> getLembretes() {
        return new ArrayList<>(lembretes);
    }

    public void marcarComoDisparado(Lembrete lembrete) {
        if (lembrete != null) {
            lembrete.setDisparado(true);
        }
    }

    public void removerLembrete(Lembrete lembrete) {
        if (lembrete == null)
            return;
        lembretes.remove(lembrete);
        atualizarListaVisual();
    }

}
