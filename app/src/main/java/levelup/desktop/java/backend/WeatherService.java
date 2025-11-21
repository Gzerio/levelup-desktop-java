package levelup.desktop.java.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeatherService {

     private static final String API_KEY =
            System.getProperty("OPENWEATHER_API_KEY");
    private static final String URL_TEMPLATE = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=pt_br&appid=%s";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

     public WeatherService() {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                    "OPENWEATHER_API_KEY não definida. " +
                    "Configure no ~/.gradle/gradle.properties ou como VM arg."
            );
        }
    }

    public static class WeatherInfo {
        public final String descricao;
        public final double tempAtual;
        public final double tempMin;
        public final double tempMax;

        public WeatherInfo(String descricao, double tempAtual, double tempMin, double tempMax) {
            this.descricao = descricao;
            this.tempAtual = tempAtual;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
        }
    }

    public static class DailyForecast {
        public final LocalDate data;
        public final double tempMin;
        public final double tempMax;

        public DailyForecast(LocalDate data, double tempMin, double tempMax) {
            this.data = data;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
        }
    }

    private static final String URL_FORECAST = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&lang=pt_br&appid=%s";

    public List<DailyForecast> buscarPrevisao4Dias(String cidade)
            throws IOException, InterruptedException {

        String cidadeEnc = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String url = String.format(URL_FORECAST, cidadeEnc, API_KEY);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar previsão: HTTP " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode lista = root.path("list");

        // agrupa por dia (data) pegando min/max do dia
        Map<LocalDate, double[]> mapa = new LinkedHashMap<>();

        for (JsonNode item : lista) {
            long dt = item.path("dt").asLong();
            LocalDate data = Instant.ofEpochSecond(dt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            double tMin = item.path("main").path("temp_min").asDouble();
            double tMax = item.path("main").path("temp_max").asDouble();

            mapa.compute(data, (d, antigo) -> {
                if (antigo == null) {
                    return new double[] { tMin, tMax };
                } else {
                    double novoMin = Math.min(antigo[0], tMin);
                    double novoMax = Math.max(antigo[1], tMax);
                    return new double[] { novoMin, novoMax };
                }
            });
        }

        List<DailyForecast> dias = new ArrayList<>();
        for (Map.Entry<LocalDate, double[]> e : mapa.entrySet()) {
            dias.add(new DailyForecast(e.getKey(), e.getValue()[0], e.getValue()[1]));
            if (dias.size() == 4)
                break;
        }

        return dias;
    }

    public WeatherInfo buscarClima(String cidade) throws IOException, InterruptedException {
        String cidadeEnc = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String url = String.format(URL_TEMPLATE, cidadeEnc, API_KEY);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar clima: HTTP " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());

        String descricao = root.path("weather").get(0).path("description").asText("");
        double tempAtual = root.path("main").path("temp").asDouble();
        double tempMin = root.path("main").path("temp_min").asDouble();
        double tempMax = root.path("main").path("temp_max").asDouble();

        return new WeatherInfo(descricao, tempAtual, tempMin, tempMax);
    }
}
