package ecommerce.modelo.pagamento;

/** Boleto: sem desconto e sem juros; pedido fica aguardando confirmação. */
public class BoletoBancario extends FormaPagamento {
    @Override public double calcularTotal(double subtotal, double frete) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean pagamentoImediato() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getDescricao() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getTipo() { throw new UnsupportedOperationException("TODO"); }
    @Override public String parametrosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
