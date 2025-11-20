package levelup.desktop.java.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    // Regex simples de e-mail (sem frescura demais)
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

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

        setErro("Login validado. Depois a gente pluga no backend, mizira. 😎");
        // aqui depois vamos chamar a API de login e abrir a tela principal
    }

    private void validarEstadoLogin() {
        if (botaoEntrar == null) return;

        String email = campoEmail != null ? campoEmail.getText().trim() : "";
        String senha = campoSenha != null ? campoSenha.getText() : "";

        boolean camposPreenchidos = !email.isEmpty() && !senha.isEmpty();
        // não precisa validar tudo pra habilitar, só não deixar vazio
        botaoEntrar.setDisable(!camposPreenchidos);
    }

    // ================== CADASTRO ==================

    @FXML
    public void aoClicarCadastrar() {
        limparErro();

        if (campoNomeCadastro == null) {
            // fallback caso ainda não tenha implementado os campos de cadastro no FXML
            setErro("Tela de cadastro ainda não foi toda ligada no FXML. 😉");
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

        setErro("Cadastro validado. Próximo passo é mandar pro backend. 🚀");
        // aqui depois vamos chamar a API de cadastro
    }

    private void validarEstadoCadastro() {
        if (botaoConfirmarCadastro == null) return;

        String nome = campoNomeCadastro != null ? campoNomeCadastro.getText().trim() : "";
        String email = campoEmailCadastro != null ? campoEmailCadastro.getText().trim() : "";
        String senha = campoSenhaCadastro != null ? campoSenhaCadastro.getText() : "";
        String confirmar = campoConfirmarSenhaCadastro != null ? campoConfirmarSenhaCadastro.getText() : "";

        boolean temAlgo = !nome.isEmpty() || !email.isEmpty() || !senha.isEmpty() || !confirmar.isEmpty();

        // Se o usuário começou a digitar, habilita; se tá tudo vazio, desabilita
        botaoConfirmarCadastro.setDisable(!temAlgo);
    }

    // ================== BOTÕES JANELA ==================

    @FXML
    private void aoClicarFechar() {
        Stage stage = (Stage) botaoFecharOuCampo().getScene().getWindow();
        stage.close();
    }

    

    // pega algum node que com certeza existe pra achar o stage
    private javafx.scene.Node botaoFecharOuCampo() {
        if (campoEmail != null) return campoEmail;
        if (campoEmailCadastro != null) return campoEmailCadastro;
        return botaoEntrar != null ? botaoEntrar : botaoConfirmarCadastro;
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
