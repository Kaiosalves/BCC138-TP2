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

    public double getTamanhoMb() {
        return this.tamanhoMb;
    }
    public void setTamanhoMb(double tamanhoMb) {
        if(tamanhoMb < 0){
            throw new IllegalArgumentException("Tamanho negativo não permitido");
        }
        this.tamanhoMb = tamanhoMb;
    }
    public String getLinkDownload() {
        return linkDownload;
    }
    public void setLinkDownload(String linkDownload) {
        if(linkDownload == null || linkDownload.trim().isEmpty()){throw new IllegalArgumentException("O link do produto não pode estar vazio");}
        this.linkDownload = linkDownload.trim();
    }

    @Override public boolean temEstoqueLimitado() {
        return false;
    }
    @Override public boolean estaDisponivel(int quantidade) {
        return true;
    }
    @Override public double calcularFreteVariavel(int quantidade) {return 0;}
    @Override public boolean exigeEntrega() {return false;}
    @Override public String getTipo() {return "DIGITAL";}
    @Override public String atributosEspecificosParaTexto() {
        return String.format(";%2f;%s", tamanhoMb, linkDownload);
    }
}
