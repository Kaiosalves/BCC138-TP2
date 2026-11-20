package ecommerce.modelo.produto;

/** Produto físico: possui peso e estoque limitado. */
public class ProdutoFisico extends Produto {

    private double pesoKg;

    public ProdutoFisico(String codigo, String nome, String descricao,
                         String categoria, double preco, int estoque, double pesoKg) {
        super(codigo, nome, descricao, categoria, preco, estoque);
        // TODO
    }

    public double getPesoKg() { 
        return this.pesoKg;
    }
    /** @throws IllegalArgumentException se peso negativo. */
    public void setPesoKg(double pesoKg) {
        if(pesoKg < 0){
            throw new IllegalArgumentException("Peso não pode ser negativo");
        }
        this.pesoKg = pesoKg;
    }

    @Override public boolean temEstoqueLimitado() {
        return true;
    }
    @Override public boolean estaDisponivel(int quantidade) {
        return quantidade <= getEstoque();
    }
    @Override public double calcularFreteVariavel(int quantidade) { 
        if(quantidade <= 0){
            throw new IllegalArgumentException("Quantidadede inválida");
        }
        if(quantidade*getPreco() >= 300.0){
            return 0;
        }
        return quantidade*pesoKg*2 +5;
    }
    @Override public boolean exigeEntrega() { 
        return true;
    }
    @Override public String getTipo() {
        return "FISICO";
    }
    @Override public String atributosEspecificosParaTexto() {
        return String.format(";%2f",pesoKg);
    }
}
