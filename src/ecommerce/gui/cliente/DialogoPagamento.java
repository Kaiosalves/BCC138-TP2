package ecommerce.gui.cliente;

import ecommerce.modelo.pagamento.FormaPagamento;
import javax.swing.JDialog;

/** Diálogo de escolha da forma de pagamento (Pix, Cartão com parcelas, Boleto) mostrando o total calculado. */
public class DialogoPagamento extends JDialog {

    private FormaPagamento formaEscolhida;

    public DialogoPagamento(java.awt.Frame dono, double subtotal, double frete) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    /** Recalcula e exibe o total ao trocar a forma/parcelas (POLIMORFISMO). */
    private void atualizarTotalExibido() { throw new UnsupportedOperationException("TODO"); }
    /** @return forma escolhida ou null se o usuário cancelou. */
    public FormaPagamento getFormaEscolhida() { throw new UnsupportedOperationException("TODO"); }
}
