package ecommerce.persistencia;

import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.modelo.pedido.Pedido;
import java.util.List;
import java.util.Map;

/** Guarda os pedidos (Map id -> Pedido) e sincroniza com pedidos.txt. */
public class RepositorioPedidos {

    private Map<Integer, Pedido> pedidos;
    private String caminhoArquivo;
    private RepositorioUsuarios repoUsuarios;   // para reconstruir o cliente de cada pedido
    private RepositorioProdutos repoProdutos;   // para reconstruir os itens de cada pedido

    public RepositorioPedidos(String caminhoArquivo, RepositorioUsuarios repoUsuarios,
                              RepositorioProdutos repoProdutos) {
        // TODO
    }

    public void carregar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
    public void salvar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    public void adicionar(Pedido pedido) { throw new UnsupportedOperationException("TODO"); }
    public Pedido buscarPorId(int id) throws PedidoInvalidoException { throw new UnsupportedOperationException("TODO"); }
    public List<Pedido> listarTodos() { throw new UnsupportedOperationException("TODO"); }
    public List<Pedido> listarPorCliente(String loginCliente) { throw new UnsupportedOperationException("TODO"); }

    /** @return próximo id disponível. */
    public int proximoId() { throw new UnsupportedOperationException("TODO"); }

    private Pedido linhaParaPedido(String linha) throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
}
