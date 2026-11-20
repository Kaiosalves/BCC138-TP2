package ecommerce.modelo.pedido;

import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.modelo.pagamento.FormaPagamento;
import ecommerce.modelo.usuario.Cliente;
import java.time.LocalDateTime;
import java.util.List;

/** Pedido finalizado por um cliente. Concentra as regras de frete e total. */
public class Pedido {

    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private FormaPagamento formaPagamento;
    private StatusPedido status;
    private LocalDateTime dataCriacao;

    public Pedido(int id, Cliente cliente, List<ItemPedido> itens, FormaPagamento formaPagamento) {
        // TODO: status inicial depende de formaPagamento.pagamentoImediato()
    }

    /** @return soma dos subtotais dos itens. */
    public double calcularSubtotal() { throw new UnsupportedOperationException("TODO"); }

    /**
     * Frete do pedido: R$ 5,00 fixos (se houver item com entrega) + soma dos fretes variáveis;
     * grátis se subtotal >= R$ 300,00; zero se só houver produtos digitais.
     */
    public double calcularFrete() { throw new UnsupportedOperationException("TODO"); }

    /** @return valor final = formaPagamento.calcularTotal(subtotal, frete). */
    public double calcularTotal() { throw new UnsupportedOperationException("TODO"); }

    /** Cancela o pedido, se a regra de status permitir. */
    public void cancelar() throws PedidoInvalidoException { throw new UnsupportedOperationException("TODO"); }

    /** @return resumo textual (itens, quantidades, valores e pagamento) para exibir na finalização. */
    public String gerarResumo() { throw new UnsupportedOperationException("TODO"); }

    public int getId() { throw new UnsupportedOperationException("TODO"); }
    public Cliente getCliente() { throw new UnsupportedOperationException("TODO"); }
    public List<ItemPedido> getItens() { throw new UnsupportedOperationException("TODO"); }
    public FormaPagamento getFormaPagamento() { throw new UnsupportedOperationException("TODO"); }
    public StatusPedido getStatus() { throw new UnsupportedOperationException("TODO"); }
    public void setStatus(StatusPedido status) { throw new UnsupportedOperationException("TODO"); }
    public LocalDateTime getDataCriacao() { throw new UnsupportedOperationException("TODO"); }

    public String paraLinhaTexto() { throw new UnsupportedOperationException("TODO"); }
}
