package ecommerce.persistencia;

import ecommerce.excecao.PersistenciaException;
import ecommerce.excecao.ProdutoNaoEncontradoException;
import ecommerce.modelo.produto.Produto;
import java.util.List;
import java.util.Map;

/** Guarda os produtos em memória (Map codigo -> Produto) e sincroniza com produtos.txt. */
public class RepositorioProdutos {

    private Map<String, Produto> produtos;
    private String caminhoArquivo;

    public RepositorioProdutos(String caminhoArquivo) {
        // TODO
    }

    /** Lê o arquivo e popula o mapa. */
    public void carregar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    /** Grava todos os produtos no arquivo. */
    public void salvar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    public void adicionar(Produto produto) { throw new UnsupportedOperationException("TODO"); }
    public Produto buscarPorCodigo(String codigo) throws ProdutoNaoEncontradoException { throw new UnsupportedOperationException("TODO"); }
    public void remover(String codigo) throws ProdutoNaoEncontradoException { throw new UnsupportedOperationException("TODO"); }
    public boolean existe(String codigo) { throw new UnsupportedOperationException("TODO"); }
    public List<Produto> listarTodos() { throw new UnsupportedOperationException("TODO"); }

    /** Converte uma linha do .txt em ProdutoFisico ou ProdutoDigital conforme o campo de tipo. */
    private Produto linhaParaProduto(String linha) throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
}
