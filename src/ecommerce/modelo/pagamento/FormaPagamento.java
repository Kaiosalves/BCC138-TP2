package ecommerce.modelo.pagamento;

/**
 * Classe abstrata das formas de pagamento.
 * Conceito: POLIMORFISMO (o Pedido chama calcularTotal() sem saber qual é a forma).
 */
public abstract class FormaPagamento {

    /**
     * Aplica desconto/juros da forma de pagamento.
     * @param subtotal soma dos itens
     * @param frete    frete já calculado pelo pedido
     * @return valor final a pagar
     */
    public abstract double calcularTotal(double subtotal, double frete);

    /** @return true se o pagamento é confirmado na hora (Pix/cartão); false se fica pendente (boleto). */
    public abstract boolean pagamentoImediato();

    /** @return nome legível, ex.: "Pix", "Cartão de crédito (4x)". */
    public abstract String getDescricao();

    /** @return rótulo usado na persistência: "PIX", "CARTAO" ou "BOLETO". */
    public abstract String getTipo();

    /** @return parâmetros extras para o .txt (ex.: nº de parcelas); vazio se não houver. */
    public abstract String parametrosParaTexto();
}
