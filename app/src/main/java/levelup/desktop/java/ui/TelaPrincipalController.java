package levelup.desktop.java.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import levelup.desktop.java.App;
import levelup.desktop.java.backend.WeatherService;
import levelup.desktop.java.backend.WeatherService.WeatherInfo;
import levelup.desktop.java.ui.efeitos.EfeitoDesfoqueDeFundo;
import levelup.desktop.java.ui.player.PlayerMusicaService;
import javafx.scene.shape.Rectangle;
import levelup.desktop.java.ui.lembretes.LembreteService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.media.AudioClip;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.event.ActionEvent;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import levelup.desktop.java.ui.TelaFocoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class TelaPrincipalController {

    @FXML
    private StackPane cardProgressoInclude;

    @FXML
    private StackPane cardProgressoBlurLayer;

    @FXML
    private ImageView cardProgressoBgImage;

    @FXML
    private Button botaoMinimizar;

    @FXML
    private Button botaoFechar;

    @FXML
    private Button botaoInicio;

    @FXML
    private Button botaoHabitos;

    @FXML
    private Button botaoEstudos;

    @FXML
    private Button botaoMetas;

    @FXML
    private StackPane conteudoCentral;

    @FXML
    private StackPane barraTopoBlurLayer;

    @FXML
    private ImageView barraTopoBgImage;

    @FXML
    private HBox barraTituloRoot;

    @FXML
    private HBox inicioContainer;

    @FXML
    private StackPane cardInicioRoot;

    @FXML
    private StackPane cardInicioBlurLayer;

    @FXML
    private ImageView cardInicioBgImage;

    @FXML
    private ImageView imagemClimaAtual;

    @FXML
    private Label labelSaudacaoUsuario;

    @FXML
    private Label labelHorarioAtual;

    @FXML
    private Label labelDataAtual;

    @FXML
    private Label labelResumoClima;

    @FXML
    private Label labelDia1, labelDia2, labelDia3, labelDia4;

    @FXML
    private Label labelMin1, labelMin2, labelMin3, labelMin4;

    @FXML
    private Label labelMax1, labelMax2, labelMax3, labelMax4;

    @FXML
    private Region barraDia1, barraDia2, barraDia3, barraDia4;

    @FXML
    private StackPane cardPlayerRoot;

    @FXML
    private StackPane cardPlayerBlurLayer;

    @FXML
    private ImageView cardPlayerBgImage;

    @FXML
    private Label labelNomePlaylist;

    @FXML
    private Label labelDescricaoPlaylist;

    @FXML
    private Button btnPlayPause;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnProxima;

    @FXML
    private Slider sliderVolume;

    @FXML
    private Slider sliderProgresso;
    @FXML
    private StackPane playerCardInclude;

    @FXML
    private Label labelTempoAtual;

    @FXML
    private Label labelTempoTotal;

    @FXML
    private ImageView imageCapaPlaylist;
    @FXML
    private StackPane appTopbar;

    @FXML
    private StackPane cardLembretesInclude;

    @FXML
    private StackPane cardLembretesBlurLayer;

    @FXML
    private ImageView cardLembretesBgImage;

    @FXML
    private StackPane cardTarefasInclude;

    @FXML
    private StackPane cardTarefasBlurLayer;

    @FXML
    private ImageView cardTarefasBgImage;

    @FXML
    private StackPane cardHabitosInclude;

    private Parent focoRoot;
    private TelaFocoController telaFocoController;
    private StackPane focoBlurLayer;
private ImageView focoBgImage;

    private StackPane cardHabitosBlurLayer;
    private ImageView cardHabitosBgImage;

    private LembreteService lembreteService;
    private AudioClip somAlertaLembrete;
    private Timeline lembretesTimeline;
    private StackPane badgeLembreteAtivo;
    private TrayIcon trayIcon;
    private Stage stageNotificacao;

    private void carregarSomAlertaLembrete() {
        var url = App.class.getResource("/audio/alerta.mp3");
        if (url != null) {
            somAlertaLembrete = new AudioClip(url.toExternalForm());
            System.out.println("[LEMBRETES] Som de alerta carregado.");
        } else {
            System.out.println("[LEMBRETES] NÃO achou /audio/alerta.mp3 no classpath.");
        }
    }

    private void carregarTelaFocoSePrecisar() {
        if (focoRoot != null) {
            return; // já carregou uma vez, reaproveita
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/TelaFoco.fxml"));
            focoRoot = loader.load();
            telaFocoController = loader.getController();

             focoBlurLayer = (StackPane) focoRoot.lookup("#cardFocoBlurLayer");
        focoBgImage   = (ImageView) focoRoot.lookup("#cardFocoBgImage");
            // se quiser, aqui você consegue chamar métodos da tela de foco depois
        } catch (IOException e) {
            e.printStackTrace();
            mostrarToast("Erro ao carregar tela de foco", false);
        }
    }

    private void mostrarTelaFoco() {
        carregarTelaFocoSePrecisar();
        if (focoRoot != null) {
            setConteudoCentral(focoRoot);
            Platform.runLater(this::atualizarBlurCardFoco);
        }
    }

    private void mostrarNotificacaoFlutuante(LembreteService.Lembrete lembrete) {
        if (lembrete == null)
            return;

        Platform.runLater(() -> {

            if (stageNotificacao != null) {
                stageNotificacao.close();
                stageNotificacao = null;
            }

            Label titulo = new Label(lembrete.getTitulo());
            titulo.getStyleClass().add("lembrete-badge-title");

            String horaStr = lembrete.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"));
            Label hora = new Label(horaStr);
            hora.getStyleClass().add("lembrete-badge-time");

            Button btnFechar = new Button("X");
            btnFechar.getStyleClass().add("lembrete-badge-close-btn");

            Region espaco = new Region();
            HBox.setHgrow(espaco, Priority.ALWAYS);

            HBox linhaTopo = new HBox(8, titulo, espaco, btnFechar);
            linhaTopo.setAlignment(Pos.CENTER_LEFT);

            VBox conteudo = new VBox(4, linhaTopo, hora);
            conteudo.getStyleClass().add("lembrete-badge-content");

            StackPane root = new StackPane(conteudo);
            root.getStyleClass().add("lembrete-badge-root");

            Scene cena = new Scene(root);
            cena.setFill(Color.TRANSPARENT);

            var cssUrl = App.class.getResource("/css/principal.css");
            if (cssUrl == null) {
                cssUrl = App.class.getResource("/principal.css");
            }

            if (cssUrl != null) {
                cena.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("[LEMBRETES] Não achei principal.css para estilizar o toast.");
            }

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);
            stage.setScene(cena);

            stage.show();

            double largura = stage.getWidth();
            double altura = stage.getHeight();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            double margem = 8;

            stage.setX(bounds.getMaxX() - largura - margem);
            stage.setY(bounds.getMaxY() - altura - margem);

            btnFechar.setOnAction(ev -> {
                pararSomAlertaLembrete();
                if (lembreteService != null) {
                    lembreteService.removerLembrete(lembrete);
                }
                stage.close();
                stageNotificacao = null;
            });

            stageNotificacao = stage;
            stage.show();
        });
    }

    private void aplicarClipNaCapa() {
        if (imageCapaPlaylist == null)
            return;

        Rectangle clip = new Rectangle();

        clip.setArcWidth(24);
        clip.setArcHeight(24);

        imageCapaPlaylist.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            clip.setWidth(newB.getWidth());
            clip.setHeight(newB.getHeight());
        });

        imageCapaPlaylist.setClip(clip);
    }

    private double offsetX;
    private double offsetY;

    private Timeline relogioTimeline;

    private final WeatherService weatherService = new WeatherService();

    private final PlayerMusicaService playerMusicaService = new PlayerMusicaService();

    private void atualizarUIFaixaAtual() {
        PlayerMusicaService.FaixaLofi faixa = playerMusicaService.getFaixaAtual();
        if (faixa == null)
            return;

        if (labelNomePlaylist != null) {
            labelNomePlaylist.setText(faixa.getTitulo());
        }
        if (labelDescricaoPlaylist != null) {
            labelDescricaoPlaylist.setText(faixa.getDescricao());
        }

        if (imageCapaPlaylist != null) {
            var urlCapa = App.class.getResource(faixa.getCaminhoCapa());
            if (urlCapa != null) {
                Image img = new Image(urlCapa.toExternalForm(), 220, 160, true, true);
                imageCapaPlaylist.setImage(img);
            } else {
                System.out.println("[PLAYER] Capa não encontrada: " + faixa.getCaminhoCapa());
            }
        }
    }

    private void inicializarPlayerCardRefsSeNecessario() {
        if (playerCardInclude == null) {
            System.out.println("[DEBUG] playerCardInclude é null (fx:include não achado)");
            return;
        }

        if (cardPlayerRoot != null && cardPlayerBlurLayer != null && cardPlayerBgImage != null) {
            return;
        }

        cardPlayerRoot = (StackPane) playerCardInclude.lookup("#cardPlayerRoot");
        cardPlayerBlurLayer = (StackPane) playerCardInclude.lookup("#cardPlayerBlurLayer");
        cardPlayerBgImage = (ImageView) playerCardInclude.lookup("#cardPlayerBgImage");

        labelNomePlaylist = (Label) playerCardInclude.lookup("#labelNomePlaylist");
        labelDescricaoPlaylist = (Label) playerCardInclude.lookup("#labelDescricaoPlaylist");
        btnPlayPause = (Button) playerCardInclude.lookup("#btnPlayPause");
        sliderVolume = (Slider) playerCardInclude.lookup("#sliderVolume");
        sliderProgresso = (Slider) playerCardInclude.lookup("#sliderProgresso");

        imageCapaPlaylist = (ImageView) playerCardInclude.lookup("#imageCapaPlaylist");
        btnAnterior = (Button) playerCardInclude.lookup("#btnAnterior");
        btnProxima = (Button) playerCardInclude.lookup("#btnProxima");
        labelTempoAtual = (Label) playerCardInclude.lookup("#labelTempoAtual");
        labelTempoTotal = (Label) playerCardInclude.lookup("#labelTempoTotal");
        imageCapaPlaylist = (ImageView) playerCardInclude.lookup("#imageCapaPlaylist");

        System.out.println("[DEBUG] cardPlayerRoot       = " + cardPlayerRoot);
        System.out.println("[DEBUG] cardPlayerBlurLayer = " + cardPlayerBlurLayer);
        System.out.println("[DEBUG] cardPlayerBgImage   = " + cardPlayerBgImage);
    }

    private void inicializarCardHabitosRefsSeNecessario() {
        if (cardHabitosInclude == null) {
            System.out.println("[DEBUG] cardHabitosInclude é null (fx:include não achado)");
            return;
        }

        if (cardHabitosBlurLayer == null || cardHabitosBgImage == null) {
            cardHabitosBlurLayer = (StackPane) cardHabitosInclude.lookup("#cardHabitosBlurLayer");
            cardHabitosBgImage = (ImageView) cardHabitosInclude.lookup("#cardHabitosBgImage");

            System.out.println("[DEBUG] cardHabitosBlurLayer = " + cardHabitosBlurLayer);
            System.out.println("[DEBUG] cardHabitosBgImage   = " + cardHabitosBgImage);
        }
    }

    private void inicializarCardTarefasRefsSeNecessario() {
        if (cardTarefasInclude == null) {
            System.out.println("[DEBUG] cardTarefasInclude é null (fx:include não achado)");
            return;
        }

        if (cardTarefasBlurLayer == null || cardTarefasBgImage == null) {
            cardTarefasBlurLayer = (StackPane) cardTarefasInclude.lookup("#cardTarefasBlurLayer");
            cardTarefasBgImage = (ImageView) cardTarefasInclude.lookup("#cardTarefasBgImage");

            System.out.println("[DEBUG] cardTarefasBlurLayer = " + cardTarefasBlurLayer);
            System.out.println("[DEBUG] cardTarefasBgImage   = " + cardTarefasBgImage);
        }
    }

    private void inicializarCardLembretesRefsSeNecessario() {
        if (cardLembretesInclude == null) {
            System.out.println("[DEBUG] cardLembretesInclude é null (fx:include não achado)");
            return;
        }

        if (cardLembretesBlurLayer == null || cardLembretesBgImage == null) {
            cardLembretesBlurLayer = (StackPane) cardLembretesInclude.lookup("#cardLembretesBlurLayer");
            cardLembretesBgImage = (ImageView) cardLembretesInclude.lookup("#cardLembretesBgImage");

            System.out.println("[DEBUG] cardLembretesBlurLayer = " + cardLembretesBlurLayer);
            System.out.println("[DEBUG] cardLembretesBgImage   = " + cardLembretesBgImage);
        }

        inicializarLembreteServiceSeNecessario();
    }

    private void inicializarCardProgressoRefsSeNecessario() {
        if (cardProgressoInclude == null) {
            System.out.println("[DEBUG] cardProgressoInclude é null (fx:include não achado)");
            return;
        }

        if (cardProgressoBlurLayer == null || cardProgressoBgImage == null) {
            cardProgressoBlurLayer = (StackPane) cardProgressoInclude.lookup("#cardProgressoBlurLayer");
            cardProgressoBgImage = (ImageView) cardProgressoInclude.lookup("#cardProgressoBgImage");

            System.out.println("[DEBUG] cardProgressoBlurLayer = " + cardProgressoBlurLayer);
            System.out.println("[DEBUG] cardProgressoBgImage   = " + cardProgressoBgImage);
        }
    }

    private void inicializarLembreteServiceSeNecessario() {
        if (cardLembretesInclude == null) {
            return;
        }
        if (lembreteService != null) {
            return;
        }

        VBox formNovoLembrete = (VBox) cardLembretesInclude.lookup("#formNovoLembrete");
        TextField campoTituloLembrete = (TextField) cardLembretesInclude.lookup("#campoTituloLembrete");
        TextField campoHorarioLembrete = (TextField) cardLembretesInclude.lookup("#campoHorarioLembrete");
        VBox listaLembretes = (VBox) cardLembretesInclude.lookup("#listaLembretes");

        Button btnSalvarLembrete = (Button) cardLembretesInclude.lookup("#btnSalvarLembrete");
        Button btnSomLembretes = (Button) cardLembretesInclude.lookup("#btnSomLembretes");
        ImageView iconeSomLembretes = (ImageView) cardLembretesInclude.lookup("#iconeSomLembretes");
        Button btnNovoLembrete = (Button) cardLembretesInclude.lookup("#btnNovoLembrete");

        Image iconeDelete = carregarImagem("/images/lixeira.png");
        Image iconeSomAtivo = carregarImagem("/images/somativo.png");
        Image iconeSomDesativado = carregarImagem("/images/somdesativado.png");

        lembreteService = new LembreteService(
                formNovoLembrete,
                campoTituloLembrete,
                campoHorarioLembrete,
                listaLembretes,
                btnSalvarLembrete,
                btnSomLembretes,
                iconeSomLembretes,
                iconeDelete,
                iconeSomAtivo,
                iconeSomDesativado,
                this::onSomLembretesTrocado);

        if (btnNovoLembrete != null) {
            btnNovoLembrete.setOnAction(e -> lembreteService.alternarFormulario());
        }
        iniciarMonitorLembretes();
    }

    private Image carregarImagem(String caminho) {
        var url = App.class.getResource(caminho);
        if (url != null) {
            return new Image(url.toExternalForm());
        }
        System.out.println("[LEMBRETES] Imagem não encontrada: " + caminho);
        return null;
    }

    private void iniciarMonitorLembretes() {
        if (lembretesTimeline != null) {
            return;
        }

        lembretesTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> verificarLembretes()));
        lembretesTimeline.setCycleCount(Timeline.INDEFINITE);
        lembretesTimeline.play();
    }

    private void verificarLembretes() {
        if (lembreteService == null)
            return;

        LocalTime agora = LocalTime.now().withSecond(0).withNano(0);

        for (LembreteService.Lembrete l : new ArrayList<>(lembreteService.getLembretes())) {
            if (!l.isDisparado() && !agora.isBefore(l.getHorario())) {
                acionarLembrete(l);
            }
        }
    }

    private void acionarLembrete(LembreteService.Lembrete lembrete) {
        if (lembrete == null || lembreteService == null)
            return;

        lembreteService.marcarComoDisparado(lembrete);

        tocarSomAlertaLembrete();
        mostrarNotificacaoFlutuante(lembrete);
    }

    private void mostrarBadgeLembrete(LembreteService.Lembrete lembrete) {
        if (conteudoCentral == null)
            return;

        if (badgeLembreteAtivo != null) {
            conteudoCentral.getChildren().remove(badgeLembreteAtivo);
            badgeLembreteAtivo = null;
        }

        Label titulo = new Label(lembrete.getTitulo());
        titulo.getStyleClass().add("lembrete-badge-title");

        String horaStr = lembrete.getHorario().format(DateTimeFormatter.ofPattern("HH:mm"));
        Label hora = new Label(horaStr);
        hora.getStyleClass().add("lembrete-badge-time");

        Button btnFechar = new Button("X");
        btnFechar.getStyleClass().add("lembrete-badge-close-btn");

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        HBox linhaTopo = new HBox(8, titulo, espaco, btnFechar);
        linhaTopo.setAlignment(Pos.CENTER_LEFT);

        VBox conteudo = new VBox(4, linhaTopo, hora);
        conteudo.getStyleClass().add("lembrete-badge-content");

        badgeLembreteAtivo = new StackPane(conteudo);
        badgeLembreteAtivo.getStyleClass().add("lembrete-badge-root");
        badgeLembreteAtivo.setMaxWidth(320);
        badgeLembreteAtivo.setMaxHeight(Region.USE_PREF_SIZE);

        conteudoCentral.getChildren().add(badgeLembreteAtivo);
        StackPane.setAlignment(badgeLembreteAtivo, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(badgeLembreteAtivo, new Insets(0, 24, 72, 0));

        btnFechar.setOnAction(e -> {
            pararSomAlertaLembrete();
            if (conteudoCentral != null && badgeLembreteAtivo != null) {
                conteudoCentral.getChildren().remove(badgeLembreteAtivo);
                badgeLembreteAtivo = null;
            }
            if (lembreteService != null) {
                lembreteService.removerLembrete(lembrete);
            }
        });
    }

    @FXML
    public void initialize() {
        System.out.println("=== DEBUG INICIAL ===");
        System.out.println("botaoInicio  = " + botaoInicio);
        System.out.println("botaoHabitos = " + botaoHabitos);
        System.out.println("botaoEstudos = " + botaoEstudos);
        System.out.println("botaoMetas   = " + botaoMetas);

        botaoHabitos.setOnMouseClicked(e -> {
            System.out.println("[DEBUG] MouseClicked direto no botaoHabitos: " + e);
        });

        marcarBotaoAtivo(botaoInicio);
        configurarArrasteJanela(barraTituloRoot);
        configurarSaudacao();
        iniciarRelogio();
        mostrarTelaInicio();
        carregarSomAlertaLembrete();

        if (conteudoCentral != null) {
            conteudoCentral.sceneProperty().addListener((obs, cenaAntiga, cenaNova) -> {
                if (cenaNova != null) {

                    cenaNova.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
                        Node n = e.getPickResult().getIntersectedNode();
                        System.out.println("[PICK] node=" + n
                                + " sceneX=" + e.getSceneX()
                                + " sceneY=" + e.getSceneY());
                    });

                    Platform.runLater(() -> {
                        if (appTopbar != null) {
                            appTopbar.toFront();
                        }

                        inicializarPlayerCardRefsSeNecessario();
                        inicializarCardLembretesRefsSeNecessario();
                        inicializarCardProgressoRefsSeNecessario();
                        inicializarCardTarefasRefsSeNecessario();
                        inicializarCardHabitosRefsSeNecessario();

                        aplicarClipNaCapa();

                        System.out.println("[DEBUG] inicioContainer.mouseTransparent = "
                                + (inicioContainer != null ? inicioContainer.isMouseTransparent() : "null"));
                        System.out.println("[DEBUG] playerCardInclude.mouseTransparent = "
                                + (playerCardInclude != null ? playerCardInclude.isMouseTransparent() : "null"));
                        System.out.println("[DEBUG] cardPlayerRoot.mouseTransparent = "
                                + (cardPlayerRoot != null ? cardPlayerRoot.isMouseTransparent() : "null"));
                        System.out.println("[DEBUG] btnPlayPause.mouseTransparent = "
                                + (btnPlayPause != null ? btnPlayPause.isMouseTransparent() : "null"));

                        EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                                barraTopoBlurLayer,
                                barraTopoBgImage,
                                40);

                        atualizarBlurCardInicio();
                        atualizarBlurPlayerCard();
                        atualizarBlurCardLembretes();
                        atualizarBlurCardProgresso();
                        atualizarBlurCardTarefas();
                        atualizarBlurCardHabitos();

                        configurarPlayerMusica();
                        buscarClimaAsync();
                    });
                }
            });
        }
    }

    public void tocarSomAlertaLembrete() {
        if (somAlertaLembrete == null) {
            System.out.println("[LEMBRETES] somAlertaLembrete é null, nada pra tocar.");
            return;
        }

        if (lembreteService != null && !lembreteService.isSomAtivo()) {
            System.out.println("[LEMBRETES] Som está desativado, não toca alerta.");
            return;
        }

        somAlertaLembrete.stop();
        somAlertaLembrete.play();
    }

    public void pararSomAlertaLembrete() {
        if (somAlertaLembrete != null) {
            somAlertaLembrete.stop();
        }
    }

    private void atualizarBlurCardTarefas() {
        if (cardTarefasBlurLayer != null && cardTarefasBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardTarefasBlurLayer,
                    cardTarefasBgImage,
                    50);
        }
    }

    private void atualizarBlurCardFoco() {
    if (focoBlurLayer != null && focoBgImage != null) {
        EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                focoBlurLayer,
                focoBgImage,
                40 // mesmo raio dos outros grandes
        );
    }
}


    private void atualizarBlurCardHabitos() {
        if (cardHabitosBlurLayer != null && cardHabitosBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardHabitosBlurLayer,
                    cardHabitosBgImage,
                    50);
        }
    }

    private void atualizarBlurCardInicio() {
        if (cardInicioBlurLayer != null && cardInicioBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardInicioBlurLayer,
                    cardInicioBgImage,
                    60);
        }
    }

    private void atualizarBlurPlayerCard() {
        System.out.println("[DEBUG] cardPlayerBlurLayer = " + cardPlayerBlurLayer);
        System.out.println("[DEBUG] cardPlayerBgImage   = " + cardPlayerBgImage);
        if (cardPlayerBlurLayer != null && cardPlayerBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardPlayerBlurLayer,
                    cardPlayerBgImage,
                    50);
        }
    }

    private void atualizarBlurCardLembretes() {
        if (cardLembretesBlurLayer != null && cardLembretesBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardLembretesBlurLayer,
                    cardLembretesBgImage,
                    50);
        }
    }

    private void atualizarBlurCardProgresso() {
        if (cardProgressoBlurLayer != null && cardProgressoBgImage != null) {
            EfeitoDesfoqueDeFundo.aplicarDesfoqueDeFundo(
                    cardProgressoBlurLayer,
                    cardProgressoBgImage,
                    50);
        }
    }

    private void configurarArrasteJanela(Node areaArraste) {
        if (areaArraste == null)
            return;

        areaArraste.setOnMousePressed(evento -> {
            Stage stage = obterStage();
            if (stage != null) {
                offsetX = evento.getSceneX();
                offsetY = evento.getSceneY();
            }
        });

        areaArraste.setOnMouseDragged(evento -> {
            Stage stage = obterStage();
            if (stage != null) {
                stage.setX(evento.getScreenX() - offsetX);
                stage.setY(evento.getScreenY() - offsetY);
            }
        });
    }

    private void onSomLembretesTrocado(boolean somAtivo) {

        if (somAtivo) {
            mostrarToast("Alerta ativo", true);

        } else {
            mostrarToast("Alerta desativado", false);
        }
    }

    private void mostrarToast(String mensagem, boolean sucesso) {
        if (conteudoCentral == null)
            return;

        Label label = new Label(mensagem);

        StackPane toast = new StackPane(label);
        toast.getStyleClass().add("toast-base");
        toast.getStyleClass().add(sucesso ? "toast-success" : "toast-error");

        toast.setMaxWidth(Region.USE_PREF_SIZE);
        toast.setMaxHeight(Region.USE_PREF_SIZE);

        conteudoCentral.getChildren().add(toast);
        StackPane.setAlignment(toast, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(toast, new Insets(0, 24, 24, 0));

        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), toast);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(2.0));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), toast);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        SequentialTransition seq = new SequentialTransition(fadeIn, pause, fadeOut);
        seq.setOnFinished(e -> conteudoCentral.getChildren().remove(toast));
        seq.play();
    }

    @FXML
    private void aoClicarMenuLembretes() {
        if (lembreteService == null) {
            inicializarLembreteServiceSeNecessario();
        }
        if (lembreteService != null) {
            lembreteService.alternarFormulario();
        }
    }

    @FXML
    private void aoClicarMinimizar() {
        Stage stage = obterStage();
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void aoClicarFechar() {
        playerMusicaService.dispose();
        Stage stage = obterStage();
        if (stage != null) {
            stage.close();
        }
    }

    private Stage obterStage() {
        if (botaoFechar != null && botaoFechar.getScene() != null) {
            return (Stage) botaoFechar.getScene().getWindow();
        }
        if (botaoMinimizar != null && botaoMinimizar.getScene() != null) {
            return (Stage) botaoMinimizar.getScene().getWindow();
        }
        if (conteudoCentral != null && conteudoCentral.getScene() != null) {
            return (Stage) conteudoCentral.getScene().getWindow();
        }
        return null;
    }

    @FXML
    private void aoClicarInicio(ActionEvent event) {
        System.out.println("[DEBUG] Clicou INÍCIO");
        marcarBotaoAtivo(botaoInicio);
        mostrarTelaInicio();

    }

    @FXML
    private void aoClicarHabitos(ActionEvent event) {
        System.out.println("[DEBUG] Clicou FOCO");
        marcarBotaoAtivo(botaoHabitos);
        mostrarTelaFoco();
    }

    @FXML
    private void aoClicarEstudos(ActionEvent event) {
        System.out.println("[DEBUG] Clicou ESTUDOS");
        marcarBotaoAtivo(botaoEstudos);
        mostrarTelaEstudos();
    }

    @FXML
    private void aoClicarMetas(ActionEvent event) {
        System.out.println("[DEBUG] Clicou METAS");
        marcarBotaoAtivo(botaoMetas);
        mostrarTelaMetas();
    }

    private void marcarBotaoAtivo(Button botaoAtivo) {
        List<Button> todos = List.of(botaoInicio, botaoHabitos, botaoEstudos, botaoMetas);

        for (Button botao : todos) {
            if (botao == null)
                continue;
            botao.getStyleClass().remove("nav-button-ativo");
            if (!botao.getStyleClass().contains("nav-button")) {
                botao.getStyleClass().add("nav-button");
            }
        }

        if (botaoAtivo != null && !botaoAtivo.getStyleClass().contains("nav-button-ativo")) {
            botaoAtivo.getStyleClass().add("nav-button-ativo");
        }
    }

    private void setConteudoCentral(Node node) {
        if (conteudoCentral == null || node == null)
            return;
        conteudoCentral.getChildren().setAll(node);
        StackPane.setAlignment(node, Pos.TOP_LEFT);
    }

    private void mostrarTelaInicio() {
        if (inicioContainer != null) {
            setConteudoCentral(inicioContainer);
        }
    }

    private Node criarPlaceholder(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("principal-placeholder");
        StackPane container = new StackPane(label);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    

    private void mostrarTelaEstudos() {
        setConteudoCentral(criarPlaceholder("Estudos — em construção"));
    }

    private void mostrarTelaMetas() {
        setConteudoCentral(criarPlaceholder("Metas — em construção"));
    }

    private void configurarSaudacao() {
        if (labelSaudacaoUsuario == null)
            return;

        String saudacao;
        int hora = LocalTime.now().getHour();
        if (hora < 12)
            saudacao = "Bom dia";
        else if (hora < 18)
            saudacao = "Boa tarde";
        else
            saudacao = "Boa noite";

        
        labelSaudacaoUsuario.setText(saudacao);
    }

    private void iniciarRelogio() {
        if (labelHorarioAtual == null || labelDataAtual == null)
            return;

        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (relogioTimeline != null) {
            relogioTimeline.stop();
        }

        relogioTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime agora = LocalTime.now();
                    LocalDate hoje = LocalDate.now();
                    labelHorarioAtual.setText(agora.format(formatoHora));
                    labelDataAtual.setText(hoje.format(formatoData));
                }),
                new KeyFrame(Duration.minutes(1)));
        relogioTimeline.setCycleCount(Timeline.INDEFINITE);
        relogioTimeline.play();
    }

    private void atualizarClimaNaTela(WeatherInfo info) {
        if (info == null || labelResumoClima == null)
            return;

        String desc = info.descricao != null ? info.descricao : "";
        if (!desc.isEmpty()) {
            desc = desc.substring(0, 1).toUpperCase() + desc.substring(1);
        }

        labelResumoClima.setText(
                String.format("%s, %.0f°C", desc, info.tempAtual));

        atualizarIconeClima(info);
    }

    private void atualizarIconeClima(WeatherInfo info) {
        if (imagemClimaAtual == null || info == null)
            return;

        String descLower = info.descricao != null
                ? info.descricao.toLowerCase(Locale.ROOT)
                : "";

        int hora = LocalTime.now().getHour();
        boolean isDia = hora >= 6 && hora < 18;

        String caminho;

        if (descLower.contains("chuva") || descLower.contains("garoa")) {
            caminho = "/weather/weather-rain.png";
        } else if (descLower.contains("tempest")) {
            caminho = "/weather/weather-storm.png";
        } else if (descLower.contains("nublado") || descLower.contains("nuvens")) {
            if (descLower.contains("poucas") || descLower.contains("dispersas")) {
                caminho = "/weather/weather-few-clouds.png";
            } else {
                caminho = "/weather/weather-cloudy.png";
            }
        } else if (descLower.contains("neve")) {
            caminho = "/weather/weather-snow.png";
        } else if (descLower.contains("névoa")
                || descLower.contains("nevoa")
                || descLower.contains("neblina")
                || descLower.contains("nevoeiro")) {
            caminho = "/weather/weather-mist.png";
        } else if (descLower.contains("limpo")
                || descLower.contains("céu claro")
                || descLower.contains("ceu claro")) {
            caminho = isDia
                    ? "/weather/weather-clear-day.png"
                    : "/weather/weather-clear-night.png";
        } else {
            caminho = isDia
                    ? "/weather/weather-clear-day.png"
                    : "/weather/weather-clear-night.png";
        }

        var stream = App.class.getResourceAsStream(caminho);
        if (stream == null) {
            System.out.println("[CLIMA] Ícone não encontrado: " + caminho);
            return;
        }

        Image img = new Image(stream, 40, 40, true, true);
        imagemClimaAtual.setImage(img);
    }

    private void buscarClimaAsync() {
        final String cidade = "Sao Paulo,BR";

        new Thread(() -> {
            try {
                WeatherInfo infoAtual = weatherService.buscarClima(cidade);
                List<WeatherService.DailyForecast> previsao = weatherService.buscarPrevisao4Dias(cidade);

                Platform.runLater(() -> {
                    atualizarClimaNaTela(infoAtual);
                    atualizarPrevisaoNaTela(previsao);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (labelResumoClima != null) {
                        labelResumoClima.setText("Não foi possível carregar o clima");
                    }
                });
            }
        }, "thread-clima").start();
    }

    private void ajustarBarra(Region barra, double tempMax,
            double globalMin, double globalMax) {
        if (barra == null)
            return;

        double trilhoTotal = 100.0;

        if (globalMax <= globalMin) {
            barra.setPrefWidth(trilhoTotal * 0.5);
            return;
        }

        double fator = (tempMax - globalMin) / (globalMax - globalMin);
        fator = Math.max(0, Math.min(1, fator));

        double larguraMin = 40;
        double larguraMax = 120;

        double largura = larguraMin + fator * (larguraMax - larguraMin);
        barra.setPrefWidth(largura);
    }

    private void atualizarPrevisaoNaTela(List<WeatherService.DailyForecast> dias) {
        if (dias == null || dias.isEmpty())
            return;

        if (dias.size() > 4) {
            dias = dias.subList(0, 4);
        }

        double minGlobal = dias.stream().mapToDouble(d -> d.tempMin).min().orElse(0);
        double maxGlobal = dias.stream().mapToDouble(d -> d.tempMax).max().orElse(1);

        Label[] diasLabels = { labelDia1, labelDia2, labelDia3, labelDia4 };
        Label[] mins = { labelMin1, labelMin2, labelMin3, labelMin4 };
        Label[] maxs = { labelMax1, labelMax2, labelMax3, labelMax4 };
        Region[] barras = { barraDia1, barraDia2, barraDia3, barraDia4 };

        Locale localePtBr = Locale.of("pt", "BR");
        DateTimeFormatter fmtDia = DateTimeFormatter.ofPattern("E", localePtBr);

        for (int i = 0; i < dias.size(); i++) {
            WeatherService.DailyForecast d = dias.get(i);

            if (diasLabels[i] != null) {
                String nomeDia = d.data.format(fmtDia);
                nomeDia = nomeDia.substring(0, 1).toUpperCase() + nomeDia.substring(1, 3);
                diasLabels[i].setText(nomeDia);
            }

            if (mins[i] != null)
                mins[i].setText(String.format("%.0f°C", d.tempMin));
            if (maxs[i] != null)
                maxs[i].setText(String.format("%.0f°C", d.tempMax));

            if (barras[i] != null) {
                ajustarBarra(barras[i], d.tempMax, minGlobal, maxGlobal);
            }
        }
    }

    private void configurarPlayerMusica() {

        atualizarUIFaixaAtual();

        if (sliderVolume != null) {
            sliderVolume.setValue(0.6);
            sliderVolume.valueProperty().addListener((obs, oldV, newV) -> {
                playerMusicaService.setVolume(newV.doubleValue());
            });
        }

        if (sliderProgresso != null && labelTempoAtual != null && labelTempoTotal != null) {
            sliderProgresso.setDisable(false);
            playerMusicaService.configurarControlesProgresso(
                    sliderProgresso,
                    labelTempoAtual,
                    labelTempoTotal);
        }

        if (btnPlayPause != null) {
            btnPlayPause.setOnAction(e -> {
                boolean tocando = playerMusicaService.playPause();
                atualizarIconePlayPause(tocando);
            });
        }

        if (btnProxima != null) {
            btnProxima.setOnAction(e -> {
                playerMusicaService.proximaFaixa();
                atualizarUIFaixaAtual();
                atualizarIconePlayPause(true);
            });
        }

        if (btnAnterior != null) {
            btnAnterior.setOnAction(e -> {
                playerMusicaService.faixaAnterior();
                atualizarUIFaixaAtual();
                atualizarIconePlayPause(true);
            });
        }

        playerMusicaService.setOnFaixaMudou(() -> Platform.runLater(this::atualizarUIFaixaAtual));

        atualizarIconePlayPause(false);
    }

    private void atualizarIconePlayPause(boolean tocando) {
        if (btnPlayPause == null)
            return;
        btnPlayPause.setText(tocando ? "⏸" : "▶");
    }

}
