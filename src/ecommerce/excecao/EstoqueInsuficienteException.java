package ecommerce.excecao;

/** Lançada quando a quantidade pedida excede o estoque disponível. */
public class EstoqueInsuficienteException extends Exception {
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
