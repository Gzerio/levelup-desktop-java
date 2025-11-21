package levelup.desktop.java.backend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthService {

    private static final String BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String login(String email, String senha)
            throws IOException, InterruptedException, AuthException {

        String json = "{"
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"senha\":\"" + escapeJson(senha) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        int status = response.statusCode();
        String body = response.body();

        if (status == 200) {
            String token = extrairToken(body);
            if (token == null) {
                throw new AuthException("Resposta do servidor não contém o token de autenticação.");
            }
            return token;
        } else if (status == 400 || status == 401) {
            throw new AuthException("Credenciais inválidas ou dados de login incorretos.");
        } else {
            throw new AuthException("Erro ao fazer login (HTTP " + status + ").");
        }
    }

    public void registrar(String nome, String email, String senha)
            throws IOException, InterruptedException, AuthException {

        String json = "{"
                + "\"nome\":\"" + escapeJson(nome) + "\","
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"senhaHash\":\"" + escapeJson(senha) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/usuarios"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        int status = response.statusCode();

        if (status == 200 || status == 201) {

            return;
        } else if (status == 409) {
            throw new AuthException("E-mail já cadastrado.");
        } else if (status == 400) {
            throw new AuthException("Dados inválidos para cadastro.");
        } else {
            throw new AuthException("Erro ao cadastrar. (HTTP " + status + ").");
        }
    }

    private String extrairToken(String body) {
        Pattern p = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(body);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String escapeJson(String valor) {
        if (valor == null)
            return "";
        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}
