package ecommerce.servico;

import ecommerce.excecao.EstoqueInsuficienteException;
import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.modelo.Carrinho;
import ecommerce.modelo.pagamento.FormaPagamento;
import ecommerce.modelo.pedido.Pedido;
import ecommerce.modelo.pedido.StatusPedido;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.persistencia.RepositorioPedidos;
import ecommerce.persistencia.RepositorioProdutos;
import java.util.List;

/** Regras de finalização, cancelamento e atualização de pedidos. */
public class PedidoService {

    private RepositorioPedidos repoPedidos;
    private RepositorioProdutos repoProdutos;

    public PedidoService(RepositorioPedidos repoPedidos, RepositorioProdutos repoProdutos) {
        // TODO
    }

    /** Valida carrinho, baixa estoque, cria o Pedido e persiste. */
    public Pedido finalizarPedido(Cliente cliente, Carrinho carrinho, FormaPagamento forma)
            throws PedidoInvalidoException, EstoqueInsuficienteException, PersistenciaException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Cancela o pedido (se permitido) e devolve as quantidades ao estoque. */
    public void cancelarPedido(int idPedido) throws PedidoInvalidoException, PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    /** Registra a confirmação do pagamento de um boleto (AGUARDANDO_PAGAMENTO -> PAGO). */
    public void confirmarPagamento(int idPedido) throws PedidoInvalidoException, PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    /** Atualiza a situação do pedido (ação do administrador). */
    public void atualizarStatus(int idPedido, StatusPedido novoStatus)
            throws PedidoInvalidoException, PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    public Pedido buscarPorId(int idPedido) throws PedidoInvalidoException { throw new UnsupportedOperationException("TODO"); }
    public List<Pedido> listarTodos() { throw new UnsupportedOperationException("TODO"); }
    public List<Pedido> listarPorCliente(Cliente cliente) { throw new UnsupportedOperationException("TODO"); }
}
