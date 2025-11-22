package levelup.desktop.java.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import levelup.desktop.java.App;

import java.net.URL;

public class GerenciadorTelas {

    private static Stage stage;
    private static StackPane root;
    private static Scene cena;

    public static void inicializar(Stage primaryStage, StackPane rootPane, Scene cenaPrincipal) {
        stage = primaryStage;
        root = rootPane;
        cena = cenaPrincipal;
    }

    private static StackPane criarContainerComFundo(Parent conteudo) {
        URL bgUrl = App.class.getResource("/images/fundo5.jpg");
        StackPane container;
        if (bgUrl != null) {
            Image bgImage = new Image(bgUrl.toExternalForm());
            ImageView bgView = new ImageView(bgImage);
            bgView.setPreserveRatio(false);
            bgView.setSmooth(true);
            bgView.setCache(true);
            bgView.fitWidthProperty().bind(root.widthProperty());
            bgView.fitHeightProperty().bind(root.heightProperty());
            container = new StackPane(bgView, conteudo);
        } else {
            container = new StackPane(conteudo);
        }
        return container;
    }

    public static void mostrarTelaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/fxml/TelaLogin.fxml"));
            Parent conteudo = loader.load();
            StackPane container = criarContainerComFundo(conteudo);

            root.getChildren().setAll(container);

            cena.getStylesheets().setAll(
                    App.class.getResource("/css/login.css").toExternalForm());

            stage.setWidth(900);
            stage.setHeight(600);
            stage.centerOnScreen();

            final double[] xOffset = { 0 };
            final double[] yOffset = { 0 };

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

    public static void mostrarTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/fxml/TelaPrincipal.fxml"));
            Parent conteudo = loader.load();

            URL bgUrl = App.class.getResource("/images/fundo11.jpg");
            StackPane container;

            if (bgUrl != null) {
                Image bgImage = new Image(bgUrl.toExternalForm());
                ImageView bgView = new ImageView(bgImage);
                bgView.setPreserveRatio(false);
                bgView.setSmooth(true);
                bgView.setCache(true);

                bgView.fitWidthProperty().bind(root.widthProperty());
                bgView.fitHeightProperty().bind(root.heightProperty());

                container = new StackPane(bgView, conteudo);
            } else {
                container = new StackPane(conteudo);
            }

            root.getChildren().setAll(container);

            cena.getStylesheets().setAll(
                    App.class.getResource("/css/principal.css").toExternalForm());

            stage.setWidth(1100);
            stage.setHeight(650);
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
