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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import levelup.desktop.java.App;
import levelup.desktop.java.backend.WeatherService;
import levelup.desktop.java.backend.WeatherService.WeatherInfo;
import levelup.desktop.java.ui.efeitos.EfeitoDesfoqueDeFundo;
import levelup.desktop.java.ui.player.PlayerMusicaService;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.event.ActionEvent;

public class TelaPrincipalController {

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

                        configurarPlayerMusica();
                        buscarClimaAsync();
                    });
                }
            });
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
        System.out.println("[DEBUG] Clicou HÁBITOS");
        marcarBotaoAtivo(botaoHabitos);
        mostrarTelaHabitos();
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

    private void mostrarTelaHabitos() {
        setConteudoCentral(criarPlaceholder("Hábitos — em construção"));
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

        String nome = "Guilherme";
        labelSaudacaoUsuario.setText(saudacao + ", " + nome);
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

        atualizarIconePlayPause(false);
    }

    private void atualizarIconePlayPause(boolean tocando) {
        if (btnPlayPause == null)
            return;
        btnPlayPause.setText(tocando ? "⏸" : "▶");
    }

}
