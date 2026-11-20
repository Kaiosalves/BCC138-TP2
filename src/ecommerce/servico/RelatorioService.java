package ecommerce.servico;

import ecommerce.modelo.pedido.StatusPedido;
import ecommerce.modelo.produto.Produto;
import ecommerce.persistencia.RepositorioPedidos;
import ecommerce.persistencia.RepositorioProdutos;
import java.util.List;
import java.util.Map;

/** Relatórios do administrador. */
public class RelatorioService {

    private RepositorioPedidos repoPedidos;
    private RepositorioProdutos repoProdutos;

    public RelatorioService(RepositorioPedidos repoPedidos, RepositorioProdutos repoProdutos) {
        // TODO
    }

    /** @return faturamento total dos pedidos não cancelados. */
    public double faturamentoTotal() { throw new UnsupportedOperationException("TODO"); }
    /** @return quantidade de pedidos por status. */
    public Map<StatusPedido, Integer> pedidosPorStatus() { throw new UnsupportedOperationException("TODO"); }
    /** @return código do produto -> quantidade total vendida. */
    public Map<String, Integer> quantidadeVendidaPorProduto() { throw new UnsupportedOperationException("TODO"); }
    /** @return produtos físicos com estoque abaixo do limite informado. */
    public List<Produto> produtosComEstoqueBaixo(int limite) { throw new UnsupportedOperationException("TODO"); }
    /** @return texto completo do relatório para exibir na GUI. */
    public String gerarRelatorioGeral() { throw new UnsupportedOperationException("TODO"); }
}
