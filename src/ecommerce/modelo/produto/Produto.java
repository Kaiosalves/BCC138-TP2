package ecommerce.modelo.produto;

import ecommerce.excecao.EstoqueInsuficienteException;

/**
 * Classe abstrata base dos produtos da loja.
 * Conceitos: HERANÇA, CLASSE ABSTRATA, ENCAPSULAMENTO, POLIMORFISMO.
 */
public abstract class Produto {

    private String codigo;
    private String nome;
    private String descricao;
    private String categoria;
    private double preco;
    private int estoque;

    /** Construtor comum; deve validar os dados via setters. */
    public Produto(String codigo, String nome, String descricao,
                   String categoria, double preco, int estoque) {
        // TODO
    }

    // ---- Métodos abstratos (cada subclasse define a sua regra) ----

    /** @return true se o estoque é limitado (físico) ou false se ilimitado (digital). */
    public abstract boolean temEstoqueLimitado();

    /** @return true se há disponibilidade para a quantidade solicitada. */
    public abstract boolean estaDisponivel(int quantidade);

    /** @return parte variável do frete para esta quantidade (R$ 2,00/kg no físico; 0 no digital). */
    public abstract double calcularFreteVariavel(int quantidade);

    /** @return true se o produto exige entrega (conta para a taxa fixa de R$ 5,00 do pedido). */
    public abstract boolean exigeEntrega();

    /** @return rótulo do tipo, ex.: "FISICO" ou "DIGITAL" (usado na persistência e na GUI). */
    public abstract String getTipo();

    /** @return atributos específicos do tipo serializados para o .txt (ex.: peso, ou tamanho;link). */
    public abstract String atributosEspecificosParaTexto();

    // ---- Estoque ----

    /** Baixa o estoque (ignorado em produto digital). */
    public void baixarEstoque(int quantidade) throws EstoqueInsuficienteException {
        // TODO
    }

    /** Devolve ao estoque (usado no cancelamento; ignorado em produto digital). */
    public void devolverEstoque(int quantidade) {
        // TODO
    }

    // ---- Getters / Setters (setters com validação) ----

    public String getCodigo() { 
        return this.codigo; 
    }
    public String getNome() { 
        return this.nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }
    public String getDescricao() { 
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        if(descricao == null || descricao.trim().isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        this.descricao = descricao.trim();
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) { 
        if(categoria == null || categoria.trim().isEmpty()){
            throw new IllegalArgumentException("Categora não pode ser vazia");
        }
        this.categoria = categoria.trim();
    }
    public double getPreco() { 
        return this.preco;
    }
    /** @throws IllegalArgumentException se preço negativo ou zero. */
    public void setPreco(double preco) { 
        if(preco <= 0){
            throw new IllegalArgumentException("Preco negativo ou zero não permitido");
        }
        this.preco = preco;
    }
    public int getEstoque() {
        return this.estoque;
    }
    /** @throws IllegalArgumentException se estoque negativo. */
    public void setEstoque(int estoque) {
        if(estoque < 0){
            throw new IllegalArgumentException("Estoque negativo não permitido");
        }
        this.estoque = estoque;
    }

    /** Serializa o produto em uma linha do arquivo produtos.txt. */
    public String paraLinhaTexto() { throw new UnsupportedOperationException("TODO"); }

    @Override
    public String toString() { throw new UnsupportedOperationException("TODO"); }
}
