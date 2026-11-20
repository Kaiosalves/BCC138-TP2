package ecommerce.modelo.pedido;

import ecommerce.modelo.produto.Produto;

/** Item de um carrinho/pedido: produto + quantidade (+ preço no momento da compra). */
public class ItemPedido {

    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido(Produto produto, int quantidade) {
        // TODO: copiar o preço atual do produto para precoUnitario
    }

    public Produto getProduto() { throw new UnsupportedOperationException("TODO"); }
    public int getQuantidade() { throw new UnsupportedOperationException("TODO"); }
    /** @throws IllegalArgumentException se quantidade menor que 1. */
    public void setQuantidade(int quantidade) { throw new UnsupportedOperationException("TODO"); }
    public double getPrecoUnitario() { throw new UnsupportedOperationException("TODO"); }

    /** @return precoUnitario * quantidade. */
    public double calcularSubtotal() { throw new UnsupportedOperationException("TODO"); }

    /** @return frete variável deste item (delega ao produto: POLIMORFISMO). */
    public double calcularFreteVariavel() { throw new UnsupportedOperationException("TODO"); }
}
