package ecommerce.excecao;

/** Lançada quando login/senha não conferem. */
public class CredenciaisInvalidasException extends Exception {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
