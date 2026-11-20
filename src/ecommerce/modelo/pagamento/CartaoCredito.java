package ecommerce.modelo.pagamento;

/** Cartão: até 6x; sem juros até 3x; a partir da 4ª parcela, juros de 2% ao mês. */
public class CartaoCredito extends FormaPagamento {

    private int parcelas;

    /** @throws IllegalArgumentException se parcelas fora do intervalo 1..6. */
    public CartaoCredito(int parcelas) {
        // TODO
    }

    public int getParcelas() { throw new UnsupportedOperationException("TODO"); }
    public void setParcelas(int parcelas) { throw new UnsupportedOperationException("TODO"); }

    /** @return valor de cada parcela (total / parcelas). */
    public double calcularValorParcela(double subtotal, double frete) { throw new UnsupportedOperationException("TODO"); }

    @Override public double calcularTotal(double subtotal, double frete) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean pagamentoImediato() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getDescricao() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getTipo() { throw new UnsupportedOperationException("TODO"); }
    @Override public String parametrosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
