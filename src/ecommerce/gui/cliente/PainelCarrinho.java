package ecommerce.gui.cliente;

import ecommerce.modelo.Carrinho;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.servico.PedidoService;
import javax.swing.JPanel;

/** Mostra o carrinho, permite alterar quantidades, remover itens e finalizar a compra. */
public class PainelCarrinho extends JPanel {

    private Carrinho carrinho;
    private Cliente cliente;
    private PedidoService pedidoService;

    public PainelCarrinho(Carrinho carrinho, Cliente cliente, PedidoService pedidoService) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    public void atualizarTabela() { throw new UnsupportedOperationException("TODO"); }
    private void aoAlterarQuantidade() { throw new UnsupportedOperationException("TODO"); }
    private void aoRemoverItem() { throw new UnsupportedOperationException("TODO"); }
    /** Abre o DialogoPagamento e, se confirmado, chama PedidoService.finalizarPedido. */
    private void aoFinalizarCompra() { throw new UnsupportedOperationException("TODO"); }
}
