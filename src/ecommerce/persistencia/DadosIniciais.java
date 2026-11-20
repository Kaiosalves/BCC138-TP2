package ecommerce.persistencia;

import ecommerce.excecao.PersistenciaException;

/** Gera os dados pré-cadastrados (admin, clientes, produtos, pedidos finalizados) na 1ª execução. */
public class DadosIniciais {

    /** Se os arquivos .txt não existirem, cria-os com a carga inicial. */
    public static void gerarSeNecessario(RepositorioUsuarios usuarios,
                                         RepositorioProdutos produtos,
                                         RepositorioPedidos pedidos) throws PersistenciaException {
        // TODO
    }

    private static void criarUsuarios(RepositorioUsuarios usuarios) { throw new UnsupportedOperationException("TODO"); }
    private static void criarProdutos(RepositorioProdutos produtos) { throw new UnsupportedOperationException("TODO"); }
    private static void criarPedidos(RepositorioPedidos pedidos) { throw new UnsupportedOperationException("TODO"); }
}
