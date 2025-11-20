package levelup.desktop.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import levelup.desktop.java.ui.GerenciadorTelas;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        double largura = 700;
        double altura = 400;
        double raioJanela = 20;

        StackPane root = new StackPane();
        Scene scene = new Scene(root, largura, altura);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        Rectangle clip = new Rectangle(largura, altura);
        clip.setArcWidth(raioJanela * 2);
        clip.setArcHeight(raioJanela * 2);

        root.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });
        root.setClip(clip);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        GerenciadorTelas.inicializar(stage, root, scene);
        GerenciadorTelas.mostrarTelaLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
