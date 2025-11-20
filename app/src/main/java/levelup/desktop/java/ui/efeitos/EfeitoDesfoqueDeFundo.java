package levelup.desktop.java.ui.efeitos;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

public class EfeitoDesfoqueDeFundo {

    public static void aplicarDesfoqueDeFundo(StackPane camadaDesfoque, ImageView imagemFundo, double raio) {
        if (camadaDesfoque == null || imagemFundo == null) return;
        if (camadaDesfoque.getScene() == null) return;

        Parent root = camadaDesfoque.getScene().getRoot();

        Node card = camadaDesfoque.getParent();
        if (card == null) return;

        boolean visOriginal = card.isVisible();
        card.setVisible(false);

        Bounds limitesCardCena = card.localToScene(card.getBoundsInLocal());
        Bounds limitesRootCena = root.localToScene(root.getBoundsInLocal());

        double x = limitesCardCena.getMinX() - limitesRootCena.getMinX();
        double y = limitesCardCena.getMinY() - limitesRootCena.getMinY();
        double largura = limitesCardCena.getWidth();
        double altura = limitesCardCena.getHeight();

        if (largura <= 0 || altura <= 0) {
            card.setVisible(visOriginal);
            return;
        }

        SnapshotParameters parametros = new SnapshotParameters();
        parametros.setViewport(new Rectangle2D(x, y, largura, altura));

        WritableImage imagemDestino = new WritableImage(
                (int) Math.ceil(largura),
                (int) Math.ceil(altura)
        );

        WritableImage snapshot = root.snapshot(parametros, imagemDestino);

        card.setVisible(visOriginal);

        if (snapshot == null) {
            return;
        }

        imagemFundo.setImage(snapshot);

        GaussianBlur desfoque = new GaussianBlur(raio);
        imagemFundo.setEffect(desfoque);
    }
}
