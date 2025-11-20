package levelup.desktop.java.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import levelup.desktop.java.backend.AuthService;
import levelup.desktop.java.backend.AuthService.AuthException;
import levelup.desktop.java.sessao.SessaoUsuario;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.util.regex.Pattern;

public class TelaLoginController {

    // ====== LOGIN (lado direito) ======
    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Button botaoEntrar;

    // Label de erro geral (por enquanto usamos pros dois fluxos)
    @FXML
    private Label labelErro;

    // ====== CADASTRO (lado esquerdo) ======
    @FXML
    private TextField campoNomeCadastro;

    @FXML
    private TextField campoEmailCadastro;

    @FXML
    private PasswordField campoSenhaCadastro;

    @FXML
    private PasswordField campoConfirmarSenhaCadastro;

    @FXML
    private Button botaoConfirmarCadastro;

    

    // Regex simples de e-mail
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    // Service para chamar o backend
    private final AuthService authService = new AuthService();

    // Flags pra não deixar os listeners reabilitarem botão enquanto request tá rolando
    private boolean loginEmAndamento = false;
    private boolean cadastroEmAndamento = false;

    @FXML
    public void initialize() {
        if (labelErro != null) {
            labelErro.setText("");
        }

        // Começa desabilitado até os campos ficarem ok
        if (botaoEntrar != null) {
            botaoEntrar.setDisable(true);
        }
        if (botaoConfirmarCadastro != null) {
            botaoConfirmarCadastro.setDisable(true);
        }

        // Listeners para habilitar/desabilitar o botão de login
        if (campoEmail != null && campoSenha != null && botaoEntrar != null) {
            campoEmail.textProperty().addListener((obs, o, n) -> validarEstadoLogin());
            campoSenha.textProperty().addListener((obs, o, n) -> validarEstadoLogin());
        }

        // Listeners para habilitar/desabilitar o botão de cadastro
        if (campoNomeCadastro != null &&
                campoEmailCadastro != null &&
                campoSenhaCadastro != null &&
                campoConfirmarSenhaCadastro != null &&
                botaoConfirmarCadastro != null) {

            campoNomeCadastro.textProperty().addListener((obs, o, n) -> validarEstadoCadastro());
            campoEmailCadastro.textProperty().addListener((obs, o, n) -> validarEstadoCadastro());
            campoSenhaCadastro.textProperty().addListener((obs, o, n) -> validarEstadoCadastro());
            campoConfirmarSenhaCadastro.textProperty().addListener((obs, o, n) -> validarEstadoCadastro());
        }
    }

    // ================== LOGIN ==================

    @FXML
    public void aoClicarEntrar() {
        limparErro();

        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText();

        String erroEmail = validarEmail(email);
        if (erroEmail != null) {
            setErro("Login: " + erroEmail);
            return;
        }

        String erroSenha = validarSenha(senha);
        if (erroSenha != null) {
            setErro("Login: " + erroSenha);
            return;
        }

        // Marca que estamos em request de login
        loginEmAndamento = true;
        if (botaoEntrar != null) {
            botaoEntrar.setDisable(true);
        }
        setErro("Login: autenticando, segura aí...");

        // Thread para não travar a UI
        new Thread(() -> {
            try {
                String token = authService.login(email, senha);

                // Guarda token na sessão
                SessaoUsuario.setTokenJwt(token);
                SessaoUsuario.setEmail(email);

                Platform.runLater(() -> {
                    loginEmAndamento = false;
                    limparErro();
                    setErro("Login ok! Depois a gente abre a tela principal, mizira. 😎");
                    validarEstadoLogin(); // reavalia habilitação do botão

                    // TODO: aqui você chama o GerenciadorTelas pra ir pra tela principal
                    // GerenciadorTelas.mostrarTelaPrincipal();
                });
            } catch (AuthException e) {
                Platform.runLater(() -> {
                    loginEmAndamento = false;
                    setErro("Login: " + e.getMessage());
                    validarEstadoLogin();
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loginEmAndamento = false;
                    setErro("Login: não consegui falar com o servidor. Confere se a API tá rodando.");
                    validarEstadoLogin();
                });
            }
        }).start();
    }

    private void validarEstadoLogin() {
        if (botaoEntrar == null) return;
        if (loginEmAndamento) return; // não mexe enquanto tá chamando backend

        String email = campoEmail != null ? campoEmail.getText().trim() : "";
        String senha = campoSenha != null ? campoSenha.getText() : "";

        boolean camposPreenchidos = !email.isEmpty() && !senha.isEmpty();
        botaoEntrar.setDisable(!camposPreenchidos);
    }

    // ================== CADASTRO ==================

    @FXML
    public void aoClicarCadastrar() {
        limparErro();

        if (campoNomeCadastro == null) {
            // fallback caso ainda não tenha implementado os campos de cadastro no FXML
            setErro("Cadastro: tela ainda não está totalmente ligada no FXML. 😉");
            return;
        }

        String nome = campoNomeCadastro.getText().trim();
        String email = campoEmailCadastro.getText().trim();
        String senha = campoSenhaCadastro.getText();
        String confirmar = campoConfirmarSenhaCadastro.getText();

        // Valida nome
        String erroNome = validarNome(nome);
        if (erroNome != null) {
            setErro("Cadastro: " + erroNome);
            return;
        }

        // Valida e-mail
        String erroEmail = validarEmail(email);
        if (erroEmail != null) {
            setErro("Cadastro: " + erroEmail);
            return;
        }

        // Valida senha
        String erroSenha = validarSenha(senha);
        if (erroSenha != null) {
            setErro("Cadastro: " + erroSenha);
            return;
        }

        // Confirmação de senha
        if (confirmar == null || confirmar.isBlank()) {
            setErro("Cadastro: confirma a senha ali embaixo também.");
            return;
        }
        if (!senha.equals(confirmar)) {
            setErro("Cadastro: senha e confirmação não batem, confere aí.");
            return;
        }

        cadastroEmAndamento = true;
        if (botaoConfirmarCadastro != null) {
            botaoConfirmarCadastro.setDisable(true);
        }
        setErro("Cadastro: enviando seus dados pro servidor...");

        new Thread(() -> {
            try {
                authService.registrar(nome, email, senha);

                Platform.runLater(() -> {
                    cadastroEmAndamento = false;
                    setErro("Cadastro feito! Agora é só logar ali à direita. 🚀");

                    // opcional: já preenche o login
                    if (campoEmail != null) campoEmail.setText(email);
                    if (campoSenha != null) campoSenha.setText(senha);

                    validarEstadoCadastro();
                    validarEstadoLogin();
                });
            } catch (AuthException e) {
                Platform.runLater(() -> {
                    cadastroEmAndamento = false;
                    setErro("Cadastro: " + e.getMessage());
                    validarEstadoCadastro();
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    cadastroEmAndamento = false;
                    setErro("Cadastro: não consegui falar com o servidor. Confere se a API tá rodando.");
                    validarEstadoCadastro();
                });
            }
        }).start();
    }

    private void validarEstadoCadastro() {
        if (botaoConfirmarCadastro == null) return;
        if (cadastroEmAndamento) return;

        String nome = campoNomeCadastro != null ? campoNomeCadastro.getText().trim() : "";
        String email = campoEmailCadastro != null ? campoEmailCadastro.getText().trim() : "";
        String senha = campoSenhaCadastro != null ? campoSenhaCadastro.getText() : "";
        String confirmar = campoConfirmarSenhaCadastro != null ? campoConfirmarSenhaCadastro.getText() : "";

        boolean temAlgo = !nome.isEmpty() || !email.isEmpty() || !senha.isEmpty() || !confirmar.isEmpty();
        botaoConfirmarCadastro.setDisable(!temAlgo);
    }

    // ================== BOTÃO FECHAR JANELA ==================

    @FXML
    private void aoClicarFechar() {
        Stage stage = (Stage) pegarAlgumNode().getScene().getWindow();
        stage.close();
    }

    // pega algum node que com certeza existe pra achar o stage
    private Node pegarAlgumNode() {
        if (campoEmail != null) return campoEmail;
        if (campoEmailCadastro != null) return campoEmailCadastro;
        if (botaoEntrar != null) return botaoEntrar;
        return botaoConfirmarCadastro;
    }

    // ================== FLUXO "ESQUECI SENHA" ==================

    @FXML
    public void aoClicarEsqueciSenha() {
        setErro("Fluxo de recuperação de senha ainda não foi implementado (mas a tela já tá pronta pra isso).");
    }

    // ================== HELPERS DE VALIDAÇÃO ==================

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "coloca seu nome ali, pelo menos.";
        }
        if (nome.length() < 3) {
            return "nome muito curto, coloca pelo menos 3 letras.";
        }
        return null;
    }

    private String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return "e-mail é obrigatório.";
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            return "esse e-mail parece estranho, confere se digitou certo.";
        }
        return null;
    }

    private String validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return "senha é obrigatória.";
        }
        if (senha.length() < 8) {
            return "senha muito curta. Usa pelo menos 8 caracteres.";
        }
        if (senha.contains(" ")) {
            return "não usa espaço na senha, mizira.";
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasUpper || !hasLower || !hasDigit) {
            return "senha precisa ter letra maiúscula, minúscula e número.";
        }

        return null;
    }

    private void setErro(String msg) {
        if (labelErro != null) {
            labelErro.setText(msg);
        }
    }

    private void limparErro() {
        if (labelErro != null) {
            labelErro.setText("");
        }
    }
}
