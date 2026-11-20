package ecommerce.servico;

import ecommerce.excecao.PersistenciaException;
import ecommerce.excecao.ProdutoNaoEncontradoException;
import ecommerce.modelo.produto.Produto;
import ecommerce.persistencia.RepositorioProdutos;
import java.util.List;

/** CRUD e consultas do catálogo. */
public class CatalogoService {

    private RepositorioProdutos repoProdutos;

    public CatalogoService(RepositorioProdutos repoProdutos) {
        // TODO
    }

    public void cadastrar(Produto produto) throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
    public void alterar(Produto produto) throws ProdutoNaoEncontradoException, PersistenciaException { throw new UnsupportedOperationException("TODO"); }
    public void remover(String codigo) throws ProdutoNaoEncontradoException, PersistenciaException { throw new UnsupportedOperationException("TODO"); }
    public Produto buscarPorCodigo(String codigo) throws ProdutoNaoEncontradoException { throw new UnsupportedOperationException("TODO"); }
    public List<Produto> listarTodos() { throw new UnsupportedOperationException("TODO"); }
    /** @return produtos disponíveis para compra (digitais sempre; físicos com estoque > 0). */
    public List<Produto> listarDisponiveis() { throw new UnsupportedOperationException("TODO"); }
    public List<Produto> buscarPorCategoria(String categoria) { throw new UnsupportedOperationException("TODO"); }
    public List<Produto> buscarPorNome(String trecho) { throw new UnsupportedOperationException("TODO"); }
}
