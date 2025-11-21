package levelup.desktop.java.ui.efeitos;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

public class EfeitoDesfoqueDeFundo {

    public static void aplicarDesfoqueDeFundo(StackPane camadaDesfoque,
            ImageView imagemFundo,
            double raio) {
        if (camadaDesfoque == null || imagemFundo == null)
            return;
        if (camadaDesfoque.getScene() == null)
            return;

        Parent root = camadaDesfoque.getScene().getRoot();

        boolean visOriginal = camadaDesfoque.isVisible();
        camadaDesfoque.setVisible(false);

        Bounds limitesBlurCena = camadaDesfoque.localToScene(camadaDesfoque.getBoundsInLocal());
        Bounds limitesRootCena = root.localToScene(root.getBoundsInLocal());

        // margem pra dentro pra não vazar nas bordas
        double margin = 2.0;

        double x = limitesBlurCena.getMinX() - limitesRootCena.getMinX() + margin;
        double y = limitesBlurCena.getMinY() - limitesRootCena.getMinY() + margin;
        double largura = limitesBlurCena.getWidth() - margin * 2;
        double altura = limitesBlurCena.getHeight() - margin * 2;

        if (largura <= 0 || altura <= 0) {
            camadaDesfoque.setVisible(visOriginal);
            return;
        }

        SnapshotParameters parametros = new SnapshotParameters();
        parametros.setViewport(new Rectangle2D(x, y, largura, altura));

        WritableImage imagemDestino = new WritableImage(
                (int) Math.ceil(largura),
                (int) Math.ceil(altura));

        WritableImage snapshot = root.snapshot(parametros, imagemDestino);

        camadaDesfoque.setVisible(visOriginal);

        if (snapshot == null) {
            return;
        }

        imagemFundo.setImage(snapshot);

        GaussianBlur desfoque = new GaussianBlur(raio);
        imagemFundo.setEffect(desfoque);
    }
}
