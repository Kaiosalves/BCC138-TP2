package ecommerce.excecao;

/** Lançada em operações inválidas sobre pedidos (carrinho vazio, cancelamento não permitido, id inexistente...). */
public class PedidoInvalidoException extends Exception {
    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
