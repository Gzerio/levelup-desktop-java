package levelup.desktop.java.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import levelup.desktop.java.App;

import java.net.URL;

public class GerenciadorTelas {

    private static Stage stage;
    private static StackPane root; // root do App
    private static Scene cena;

    // chamada uma vez no start do App
    public static void inicializar(Stage primaryStage, StackPane rootPane, Scene cenaPrincipal) {
        stage = primaryStage;
        root = rootPane;
        cena = cenaPrincipal;
    }

    // ====== TELA DE LOGIN ======
    public static void mostrarTelaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/fxml/TelaLogin.fxml")
            );
            Parent conteudo = loader.load();

            // carregar imagem de fundo com blur
            URL bgUrl = App.class.getResource("/images/fundo12.jpg");
            StackPane container;

            if (bgUrl != null) {
                Image bgImage = new Image(bgUrl.toExternalForm());
                ImageView bgView = new ImageView(bgImage);
                bgView.setPreserveRatio(false);
                bgView.setSmooth(true);
                bgView.setCache(true);

                // blur suave (você já tinha colocado 2)
                bgView.setEffect(new GaussianBlur(5));

                // imagem acompanha o tamanho do root
                bgView.fitWidthProperty().bind(root.widthProperty());
                bgView.fitHeightProperty().bind(root.heightProperty());

                // empilha: imagem borrada atrás + conteúdo por cima
                container = new StackPane(bgView, conteudo);
            } else {
                System.err.println("ATENÇÃO: Imagem /images/fundo1.jpg não encontrada no classpath.");
                container = new StackPane(conteudo);
            }

            // troca o conteúdo visível
            root.getChildren().setAll(container);

            // carrega CSS específico do login
            cena.getStylesheets().setAll(
                    App.class.getResource("/css/login.css").toExternalForm()
            );

            // tamanho da janela pra tela de login
            stage.setWidth(700);
            stage.setHeight(400);
            stage.centerOnScreen();

            // arrastar janela pelo root
            final double[] xOffset = {0};
            final double[] yOffset = {0};

            root.setOnMousePressed(event -> {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset[0]);
                stage.setY(event.getScreenY() - yOffset[0]);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== FUTURA TELA PRINCIPAL (Dashboard) ======
    // Quando você criar TelaPrincipal.fxml, é só implementar isso aqui:
    /*
    public static void mostrarTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/fxml/TelaPrincipal.fxml")
            );
            Parent conteudo = loader.load();

            root.getChildren().setAll(conteudo);

            cena.getStylesheets().setAll(
                    App.class.getResource("/css/principal.css").toExternalForm()
            );

            stage.setWidth(1100);
            stage.setHeight(700);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
