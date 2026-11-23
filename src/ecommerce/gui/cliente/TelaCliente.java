package ecommerce.gui.cliente;

import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.gui.GuiUtils;
import ecommerce.modelo.Carrinho;
import ecommerce.modelo.pedido.Pedido;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.servico.CatalogoService;
import ecommerce.servico.PedidoService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/** Janela principal do cliente (abas: Catálogo, Carrinho, Meus pedidos). */
public class TelaCliente extends JFrame {

    private Cliente cliente;
    private CatalogoService catalogoService;
    private PedidoService pedidoService;
    private Carrinho carrinho;

    private JTabbedPane abas;
    private PainelCatalogo painelCatalogo;
    private PainelCarrinho painelCarrinho;

    private List<Pedido> meusPedidos;
    private JTable tabelaPedidos;
    private DefaultTableModel modeloPedidos;
    private JTextArea areaResumoPedido;

    public TelaCliente(Cliente cliente, CatalogoService catalogo, PedidoService pedidos) {
        this.cliente = cliente;
        this.catalogoService = catalogo;
        this.pedidoService = pedidos;
        this.carrinho = new Carrinho();
        this.meusPedidos = new ArrayList<>();
        montarComponentes();
    }

    private void montarComponentes() {
        String nomeCliente = cliente != null ? cliente.getNome() : "Cliente";
        setTitle("Loja Virtual — Bem-vindo(a), " + nomeCliente);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Cabeçalho com identificação do cliente e botão Sair
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(8, 14, 4, 14));

        String endereco = cliente != null && cliente.getEndereco() != null
                ? " | Entrega: " + cliente.getEndereco()
                : "";
        JLabel lblCliente = new JLabel("Cliente: " + nomeCliente + endereco);
        lblCliente.setFont(GuiUtils.FONTE_SUBTITULO);
        painelTopo.add(lblCliente, BorderLayout.WEST);

        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> sair());
        painelSair.add(btnSair);
        painelTopo.add(painelSair, BorderLayout.EAST);

        add(painelTopo, BorderLayout.NORTH);

        // Abas do cliente
        painelCatalogo = new PainelCatalogo(catalogoService, carrinho, this::atualizarTituloAbaCarrinho);
        painelCarrinho = new PainelCarrinho(carrinho, cliente, pedidoService, this::aoAtualizarCarrinho);
        JPanel painelMeusPedidos = criarPainelMeusPedidos();

        abas = new JTabbedPane();
        abas.addTab("Catálogo", painelCatalogo);
        abas.addTab("Carrinho", painelCarrinho);
        abas.addTab("Meus pedidos", painelMeusPedidos);

        abas.addChangeListener(e -> {
            int idx = abas.getSelectedIndex();
            if (idx == 0) {
                painelCatalogo.atualizarLista();
            } else if (idx == 1) {
                painelCarrinho.atualizarTabela();
            } else if (idx == 2) {
                atualizarMeusPedidos();
            }
        });

        add(abas, BorderLayout.CENTER);
        atualizarTituloAbaCarrinho();

        setMinimumSize(new Dimension(920, 600));
        setSize(1020, 660);
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelMeusPedidos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloPedidos = GuiUtils.criarModeloTabela(
                "ID", "Data", "Pagamento", "Subtotal", "Frete", "Total", "Status");
        tabelaPedidos = new JTable(modeloPedidos);
        GuiUtils.configurarTabela(tabelaPedidos);
        tabelaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                exibirResumoPedidoSelecionado();
            }
        });
        JScrollPane scrollTabela = new JScrollPane(tabelaPedidos);
        scrollTabela.setPreferredSize(new Dimension(600, 280));

        areaResumoPedido = new JTextArea();
        areaResumoPedido.setEditable(false);
        areaResumoPedido.setFont(GuiUtils.FONTE_MONOSPACE);
        areaResumoPedido.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scrollResumo = new JScrollPane(areaResumoPedido);
        scrollResumo.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido"));
        scrollResumo.setPreferredSize(new Dimension(600, 180));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabela, scrollResumo);
        split.setResizeWeight(0.58);
        painel.add(split, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnCancelarPedido = new JButton("Cancelar Pedido");

        btnAtualizar.addActionListener(e -> atualizarMeusPedidos());
        btnCancelarPedido.addActionListener(e -> aoCancelarPedidoSelecionado());

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnCancelarPedido);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private void aoAtualizarCarrinho() {
        atualizarTituloAbaCarrinho();
        atualizarMeusPedidos();
    }

    private void atualizarTituloAbaCarrinho() {
        if (abas == null || abas.getTabCount() < 2) {
            return;
        }
        try {
            int qtdItens = carrinho != null && carrinho.getItens() != null
                    ? carrinho.getItens().size()
                    : 0;
            abas.setTitleAt(1, qtdItens > 0 ? "Carrinho (" + qtdItens + ")" : "Carrinho");
        } catch (RuntimeException ignored) {
            abas.setTitleAt(1, "Carrinho");
        }
    }

    private void atualizarMeusPedidos() {
        modeloPedidos.setRowCount(0);
        meusPedidos.clear();
        areaResumoPedido.setText("");

        if (pedidoService == null || cliente == null) {
            return;
        }

        try {
            List<Pedido> lista = pedidoService.listarPorCliente(cliente);
            if (lista != null) {
                meusPedidos.addAll(lista);
            }
            for (Pedido p : meusPedidos) {
                String formaPag = p.getFormaPagamento() != null ? p.getFormaPagamento().getDescricao() : "-";
                modeloPedidos.addRow(new Object[]{
                        p.getId(),
                        GuiUtils.formatarDataHora(p.getDataCriacao()),
                        formaPag,
                        GuiUtils.formatarMoeda(p.calcularSubtotal()),
                        GuiUtils.formatarMoeda(p.calcularFrete()),
                        GuiUtils.formatarMoeda(p.calcularTotal()),
                        p.getStatus()
                });
            }
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao carregar seus pedidos: " + e.getMessage());
        }
    }

    private Pedido getPedidoSelecionado() {
        int linha = tabelaPedidos.getSelectedRow();
        if (linha < 0 || linha >= meusPedidos.size()) {
            return null;
        }
        return meusPedidos.get(linha);
    }

    private void exibirResumoPedidoSelecionado() {
        Pedido selecionado = getPedidoSelecionado();
        if (selecionado == null) {
            areaResumoPedido.setText("");
            return;
        }
        areaResumoPedido.setText(selecionado.gerarResumo());
        areaResumoPedido.setCaretPosition(0);
    }

    private void aoCancelarPedidoSelecionado() {
        Pedido selecionado = getPedidoSelecionado();
        if (selecionado == null) {
            GuiUtils.mostrarAviso(this, "Selecione um pedido para cancelar.");
            return;
        }
        if (!selecionado.getStatus().permiteCancelamento()) {
            GuiUtils.mostrarAviso(
                    this,
                    "Este pedido está no status " + selecionado.getStatus() + " e não pode ser cancelado.");
            return;
        }

        boolean confirmado = GuiUtils.confirmar(
                this,
                "Deseja realmente cancelar o pedido #" + selecionado.getId() + "?",
                "Cancelar Pedido");
        if (!confirmado) {
            return;
        }

        try {
            pedidoService.cancelarPedido(selecionado.getId());
            GuiUtils.mostrarInfo(this, "Pedido #" + selecionado.getId() + " cancelado com sucesso.");
            atualizarMeusPedidos();
            painelCatalogo.atualizarLista();
        } catch (PedidoInvalidoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao cancelar pedido: " + e.getMessage());
        }
    }

    private void sair() {
        if (GuiUtils.confirmar(this, "Deseja sair da sua conta?", "Sair")) {
            dispose();
        }
    }
}
