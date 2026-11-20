package ecommerce.modelo.produto;

/** Produto físico: possui peso e estoque limitado. */
public class ProdutoFisico extends Produto {

    private double pesoKg;

    public ProdutoFisico(String codigo, String nome, String descricao,
                         String categoria, double preco, int estoque, double pesoKg) {
        super(codigo, nome, descricao, categoria, preco, estoque);
        // TODO
    }

    public double getPesoKg() { throw new UnsupportedOperationException("TODO"); }
    /** @throws IllegalArgumentException se peso negativo. */
    public void setPesoKg(double pesoKg) { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean temEstoqueLimitado() { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean estaDisponivel(int quantidade) { throw new UnsupportedOperationException("TODO"); }
    @Override public double calcularFreteVariavel(int quantidade) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean exigeEntrega() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getTipo() { throw new UnsupportedOperationException("TODO"); }
    @Override public String atributosEspecificosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
