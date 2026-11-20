package ecommerce.modelo;

import ecommerce.excecao.EstoqueInsuficienteException;
import ecommerce.excecao.ProdutoNaoEncontradoException;
import ecommerce.modelo.pedido.ItemPedido;
import ecommerce.modelo.produto.Produto;
import java.util.List;

/** Carrinho de compras de um cliente (COLEÇÃO de ItemPedido). */
public class Carrinho {

    private List<ItemPedido> itens;

    public Carrinho() {
        // TODO: inicializar a lista
    }

    /** Adiciona (ou soma à quantidade existente), respeitando o estoque. */
    public void adicionar(Produto produto, int quantidade) throws EstoqueInsuficienteException {
        // TODO
    }

    /** Remove o produto do carrinho. */
    public void remover(String codigoProduto) throws ProdutoNaoEncontradoException {
        // TODO
    }

    /** Altera a quantidade de um item, respeitando o estoque. */
    public void alterarQuantidade(String codigoProduto, int novaQuantidade)
            throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        // TODO
    }

    public double calcularSubtotal() { throw new UnsupportedOperationException("TODO"); }
    public boolean estaVazio() { throw new UnsupportedOperationException("TODO"); }
    public void limpar() { throw new UnsupportedOperationException("TODO"); }

    /** @return cópia/visão imutável dos itens (ENCAPSULAMENTO). */
    public List<ItemPedido> getItens() { throw new UnsupportedOperationException("TODO"); }
}
