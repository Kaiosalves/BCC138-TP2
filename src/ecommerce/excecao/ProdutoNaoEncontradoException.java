package ecommerce.excecao;

/** Lançada quando um código de produto não existe no catálogo. */
public class ProdutoNaoEncontradoException extends Exception {
    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
