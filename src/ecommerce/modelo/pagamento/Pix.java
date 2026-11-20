package ecommerce.modelo.pagamento;

/** Pix: 5% de desconto sobre o total do pedido. */
public class Pix extends FormaPagamento {
    @Override public double calcularTotal(double subtotal, double frete) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean pagamentoImediato() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getDescricao() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getTipo() { throw new UnsupportedOperationException("TODO"); }
    @Override public String parametrosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
