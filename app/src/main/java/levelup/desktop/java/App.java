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

        // root inicial vazio; o GerenciadorTelas vai colocar a tela dentro dele
        StackPane root = new StackPane();
        Scene scene = new Scene(root, largura, altura);

        // janela sem borda e fundo transparente
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        // clip arredondado pra cortar cantos
        Rectangle clip = new Rectangle(largura, altura);
clip.setArcWidth(raioJanela * 2);   // 40
clip.setArcHeight(raioJanela * 2);  // 40

root.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
    clip.setWidth(newBounds.getWidth());
    clip.setHeight(newBounds.getHeight());
});
root.setClip(clip);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        // inicializa gerenciador de telas
        GerenciadorTelas.inicializar(stage, root, scene);

        // primeira tela -> login
        GerenciadorTelas.mostrarTelaLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
