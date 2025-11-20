package levelup.desktop.java.sessao;

public class SessaoUsuario {

    private static String tokenJwt;
    private static String email;
//
    private SessaoUsuario() {
        
    }

    public static String getTokenJwt() {
        return tokenJwt;
    }

    public static void setTokenJwt(String tokenJwt) {
        SessaoUsuario.tokenJwt = tokenJwt;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SessaoUsuario.email = email;
    }

    public static void limpar() {
        tokenJwt = null;
        email = null;
    }
}
