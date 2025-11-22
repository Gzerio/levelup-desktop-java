package levelup.desktop.java.ui.player;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import levelup.desktop.java.App;

import java.net.URL;
import java.util.List;

public class PlayerMusicaService {

    public static class FaixaLofi {
        private final String id;
        private final String titulo;
        private final String descricao;
        private final String caminhoAudio;
        private final String caminhoCapa;

        public FaixaLofi(String id,
                String titulo,
                String descricao,
                String caminhoAudio,
                String caminhoCapa) {
            this.id = id;
            this.titulo = titulo;
            this.descricao = descricao;
            this.caminhoAudio = caminhoAudio;
            this.caminhoCapa = caminhoCapa;
        }

        public String getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getCaminhoAudio() {
            return caminhoAudio;
        }

        public String getCaminhoCapa() {
            return caminhoCapa;
        }
    }

    private final List<FaixaLofi> playlist = List.of(
            new FaixaLofi(
                    "lofi_back",
                    "Coffee Lofi",
                    "Batidas calmas para começar o dia",
                    "/audio/lofi_back.mp3",
                    "/capas/lofi_back.jpg"),
            new FaixaLofi(
                    "lofi_city",
                    "Lofi City",
                    "Vibes de cidade à noite",
                    "/audio/lofi_city.mp3",
                    "/capas/lofi_city.png"),
            new FaixaLofi(
                    "lofi_hiphop",
                    "Lofi Hip Hop",
                    "Lofi + hip hop para focar",
                    "/audio/lofi_hiphop.mp3",
                    "/capas/lofi_hiphop.png"),
            new FaixaLofi(
                    "lofi1",
                    "Lofi Dance",
                    "Faixa lofi Dance",
                    "/audio/lofi1.mp3",
                    "/capas/lofi1.jpg"),
            new FaixaLofi(
                    "lofi2",
                    "Lofi Chill",
                    "Faixa lofi Chill",
                    "/audio/lofi2.mp3",
                    "/capas/lofi2.jpg"),
            new FaixaLofi(
                    "lofi_ambi",
                    "Lofi Ambient",
                    "Ambiente relax",
                    "/audio/lofiambi.mp3",
                    "/capas/lofiambi.jpg"));

    private int indiceAtual = 0;
    private MediaPlayer mediaPlayer;

    private Slider sliderProgresso;
    private Label labelTempoAtual;
    private Label labelTempoTotal;

    public void configurarControlesProgresso(Slider slider, Label tempoAtual, Label tempoTotal) {
        this.sliderProgresso = slider;
        this.labelTempoAtual = tempoAtual;
        this.labelTempoTotal = tempoTotal;

        if (mediaPlayer != null) {
            configurarBindingsDeTempo();
        }
    }

    private Media criarMedia(FaixaLofi faixa) {
        URL recurso = App.class.getResource(faixa.getCaminhoAudio());
        if (recurso == null) {
            System.out.println("[PLAYER] Áudio não encontrado: " + faixa.getCaminhoAudio());
            return null;
        }
        return new Media(recurso.toExternalForm());
    }

    private void trocarParaIndice(int novoIndice, boolean autoPlay) {
        if (playlist.isEmpty())
            return;

        if (novoIndice < 0 || novoIndice >= playlist.size()) {
            novoIndice = 0;
        }
        indiceAtual = novoIndice;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        Media media = criarMedia(getFaixaAtual());
        if (media == null)
            return;

        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnEndOfMedia(this::proximaFaixa);

        configurarBindingsDeTempo();

        if (autoPlay) {
            mediaPlayer.play();
        }
    }

    public FaixaLofi getFaixaAtual() {
        if (playlist.isEmpty())
            return null;
        return playlist.get(indiceAtual);
    }

    public boolean playPause() {

        if (mediaPlayer == null) {
            trocarParaIndice(indiceAtual, true);
            return true;
        }

        MediaPlayer.Status status = mediaPlayer.getStatus();

        if (status == MediaPlayer.Status.PLAYING) {

            mediaPlayer.pause();
            return false;
        } else {

            mediaPlayer.play();
            return true;
        }
    }

    public void proximaFaixa() {
        if (playlist.isEmpty())
            return;
        int novo = (indiceAtual + 1) % playlist.size();
        trocarParaIndice(novo, true);
    }

    public void faixaAnterior() {
        if (playlist.isEmpty())
            return;
        int novo = indiceAtual - 1;
        if (novo < 0) {
            novo = playlist.size() - 1;
        }
        trocarParaIndice(novo, true);
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public boolean estaTocando() {
        return mediaPlayer != null
                && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public Duration getDuracaoTotal() {
        if (mediaPlayer == null)
            return Duration.UNKNOWN;
        return mediaPlayer.getTotalDuration();
    }

    public Duration getTempoAtual() {
        if (mediaPlayer == null)
            return Duration.ZERO;
        return mediaPlayer.getCurrentTime();
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void configurarBindingsDeTempo() {
        if (mediaPlayer == null || sliderProgresso == null) {
            return;
        }

        mediaPlayer.setOnReady(() -> {
            Duration total = mediaPlayer.getMedia().getDuration();
            double totalSegundos = total.toSeconds();

            Platform.runLater(() -> {
                sliderProgresso.setMin(0);
                sliderProgresso.setMax(totalSegundos);
                sliderProgresso.setValue(0);

                if (labelTempoAtual != null) {
                    labelTempoAtual.setText("0:00");
                }
                if (labelTempoTotal != null) {
                    labelTempoTotal.setText(formatarTempo(total));
                }
            });
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (sliderProgresso != null && !sliderProgresso.isValueChanging()) {
                double segundos = newTime.toSeconds();
                Platform.runLater(() -> sliderProgresso.setValue(segundos));
            }

            if (labelTempoAtual != null) {
                Platform.runLater(() -> labelTempoAtual.setText(formatarTempo(newTime)));
            }
        });

        if (sliderProgresso != null) {
            sliderProgresso.valueChangingProperty().addListener((obs, wasChanging, changing) -> {
                if (!changing && mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(sliderProgresso.getValue()));
                }
            });

            sliderProgresso.setOnMouseReleased(e -> {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(sliderProgresso.getValue()));
                }
            });
        }
    }

    private String formatarTempo(Duration dur) {
        if (dur == null || dur.isUnknown()) {
            return "0:00";
        }

        int totalSegundos = (int) Math.floor(dur.toSeconds());
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;

        return String.format("%d:%02d", minutos, segundos);
    }
}
