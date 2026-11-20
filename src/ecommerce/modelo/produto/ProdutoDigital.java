package ecommerce.modelo.produto;

/** Produto digital: tamanho do arquivo, link de download e estoque ilimitado. */
public class ProdutoDigital extends Produto {

    private double tamanhoMb;
    private String linkDownload;

    public ProdutoDigital(String codigo, String nome, String descricao,
                          String categoria, double preco, double tamanhoMb, String linkDownload) {
        super(codigo, nome, descricao, categoria, preco, 0);
        // TODO
    }

    public double getTamanhoMb() { throw new UnsupportedOperationException("TODO"); }
    public void setTamanhoMb(double tamanhoMb) { throw new UnsupportedOperationException("TODO"); }
    public String getLinkDownload() { throw new UnsupportedOperationException("TODO"); }
    public void setLinkDownload(String linkDownload) { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean temEstoqueLimitado() { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean estaDisponivel(int quantidade) { throw new UnsupportedOperationException("TODO"); }
    @Override public double calcularFreteVariavel(int quantidade) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean exigeEntrega() { throw new UnsupportedOperationException("TODO"); }
    @Override public String getTipo() { throw new UnsupportedOperationException("TODO"); }
    @Override public String atributosEspecificosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
