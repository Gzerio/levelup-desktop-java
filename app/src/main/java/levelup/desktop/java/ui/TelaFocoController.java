package levelup.desktop.java.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.event.ActionEvent;

public class TelaFocoController {

    @FXML
    private ComboBox<String> comboTarefas;

    @FXML
    private Label labelEstado;

    @FXML
    private Label labelTimer;

    @FXML
    private ProgressBar progressSessao;

    @FXML
    private Button botaoFoco;

    @FXML
    private Button botaoPausar;

    @FXML
    public void initialize() {
        // só pra não quebrar
        if (labelTimer != null) {
            labelTimer.setText("25:00");
        }
        if (labelEstado != null) {
            labelEstado.setText("Pronto para focar");
        }
    }

    @FXML
    private void aoClicarIniciarOuEncerrar(ActionEvent event) {
        System.out.println("[FOCO] Clicou iniciar/encerrar");
        // depois a gente mete o timer aqui
    }

    @FXML
    private void aoClicarPausar(ActionEvent event) {
        System.out.println("[FOCO] Clicou pausar");
    }
}
