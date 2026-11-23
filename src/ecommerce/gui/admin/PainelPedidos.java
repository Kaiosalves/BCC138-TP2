package ecommerce.gui.admin;

import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.gui.GuiUtils;
import ecommerce.modelo.pedido.Pedido;
import ecommerce.modelo.pedido.StatusPedido;
import ecommerce.servico.PedidoService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/** Lista todos os pedidos; permite atualizar status, confirmar boleto e cancelar. */
public class PainelPedidos extends JPanel {

    private static final String FILTRO_TODOS = "Todos";

    private PedidoService pedidoService;
    private List<Pedido> pedidosExibidos;

    private JComboBox<Object> comboFiltroStatus;
    private JTable tabelaPedidos;
    private DefaultTableModel modeloTabela;
    private JTextArea areaResumo;
    private JComboBox<StatusPedido> comboNovoStatus;

    public PainelPedidos(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
        this.pedidosExibidos = new ArrayList<>();
        montarComponentes();
        atualizarTabela();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra superior com filtro e atualização
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelTopo.add(new JLabel("Filtrar por status:"));
        comboFiltroStatus = new JComboBox<>();
        comboFiltroStatus.addItem(FILTRO_TODOS);
        for (StatusPedido status : StatusPedido.values()) {
            comboFiltroStatus.addItem(status);
        }
        comboFiltroStatus.addActionListener(e -> atualizarTabela());

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> atualizarTabela());

        painelTopo.add(comboFiltroStatus);
        painelTopo.add(btnAtualizar);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela de pedidos
        modeloTabela = GuiUtils.criarModeloTabela(
                "ID", "Data", "Cliente", "Pagamento", "Subtotal", "Frete", "Total", "Status");
        tabelaPedidos = new JTable(modeloTabela);
        GuiUtils.configurarTabela(tabelaPedidos);
        tabelaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                exibirResumoSelecionado();
            }
        });
        JScrollPane scrollTabela = new JScrollPane(tabelaPedidos);
        scrollTabela.setPreferredSize(new Dimension(600, 280));

        // Área de resumo do pedido selecionado
        areaResumo = new JTextArea();
        areaResumo.setEditable(false);
        areaResumo.setFont(GuiUtils.FONTE_MONOSPACE);
        areaResumo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scrollResumo = new JScrollPane(areaResumo);
        scrollResumo.setBorder(BorderFactory.createTitledBorder("Resumo do Pedido Selecionado"));
        scrollResumo.setPreferredSize(new Dimension(600, 180));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabela, scrollResumo);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);

        // Barra inferior de ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        painelAcoes.add(new JLabel("Novo status:"));
        comboNovoStatus = new JComboBox<>(StatusPedido.values());

        JButton btnAtualizarStatus = new JButton("Atualizar Status");
        JButton btnConfirmarPagamento = new JButton("Confirmar Pagamento (Boleto)");
        JButton btnCancelar = new JButton("Cancelar Pedido");

        btnAtualizarStatus.addActionListener(e -> aoAtualizarStatus());
        btnConfirmarPagamento.addActionListener(e -> aoConfirmarPagamento());
        btnCancelar.addActionListener(e -> aoCancelar());

        painelAcoes.add(comboNovoStatus);
        painelAcoes.add(btnAtualizarStatus);
        painelAcoes.add(btnConfirmarPagamento);
        painelAcoes.add(btnCancelar);
        add(painelAcoes, BorderLayout.SOUTH);
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        pedidosExibidos.clear();
        areaResumo.setText("");

        if (pedidoService == null) {
            return;
        }

        try {
            Object filtro = comboFiltroStatus != null ? comboFiltroStatus.getSelectedItem() : FILTRO_TODOS;
            List<Pedido> todos = pedidoService.listarTodos();
            if (todos != null) {
                for (Pedido p : todos) {
                    if (FILTRO_TODOS.equals(filtro) || p.getStatus() == filtro) {
                        pedidosExibidos.add(p);
                    }
                }
            }

            for (Pedido p : pedidosExibidos) {
                String nomeCliente = p.getCliente() != null ? p.getCliente().getNome() : "-";
                String formaPag = p.getFormaPagamento() != null ? p.getFormaPagamento().getDescricao() : "-";
                modeloTabela.addRow(new Object[]{
                        p.getId(),
                        GuiUtils.formatarDataHora(p.getDataCriacao()),
                        nomeCliente,
                        formaPag,
                        GuiUtils.formatarMoeda(p.calcularSubtotal()),
                        GuiUtils.formatarMoeda(p.calcularFrete()),
                        GuiUtils.formatarMoeda(p.calcularTotal()),
                        p.getStatus()
                });
            }
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao listar pedidos: " + e.getMessage());
        }
    }

    private void exibirResumoSelecionado() {
        Pedido selecionado = getPedidoSelecionado();
        if (selecionado == null) {
            areaResumo.setText("");
            return;
        }
        areaResumo.setText(selecionado.gerarResumo());
        areaResumo.setCaretPosition(0);
        if (selecionado.getStatus() != null) {
            comboNovoStatus.setSelectedItem(selecionado.getStatus());
        }
    }

    private Pedido getPedidoSelecionado() {
        int linha = tabelaPedidos.getSelectedRow();
        if (linha < 0 || linha >= pedidosExibidos.size()) {
            return null;
        }
        return pedidosExibidos.get(linha);
    }

    private Integer exigirIdPedidoSelecionado() {
        Pedido selecionado = getPedidoSelecionado();
        if (selecionado == null) {
            GuiUtils.mostrarAviso(this, "Selecione um pedido na tabela.");
            return null;
        }
        return selecionado.getId();
    }

    private void aoAtualizarStatus() {
        Integer idPedido = exigirIdPedidoSelecionado();
        if (idPedido == null) {
            return;
        }
        StatusPedido novoStatus = (StatusPedido) comboNovoStatus.getSelectedItem();
        if (novoStatus == null) {
            return;
        }

        try {
            pedidoService.atualizarStatus(idPedido, novoStatus);
            GuiUtils.mostrarInfo(this, "Status do pedido #" + idPedido + " atualizado para " + novoStatus + ".");
            atualizarTabela();
        } catch (PedidoInvalidoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao atualizar status: " + e.getMessage());
        }
    }

    private void aoConfirmarPagamento() {
        Integer idPedido = exigirIdPedidoSelecionado();
        if (idPedido == null) {
            return;
        }

        try {
            pedidoService.confirmarPagamento(idPedido);
            GuiUtils.mostrarInfo(this, "Pagamento do pedido #" + idPedido + " confirmado com sucesso!");
            atualizarTabela();
        } catch (PedidoInvalidoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao confirmar pagamento: " + e.getMessage());
        }
    }

    private void aoCancelar() {
        Integer idPedido = exigirIdPedidoSelecionado();
        if (idPedido == null) {
            return;
        }

        boolean confirmado = GuiUtils.confirmar(
                this,
                "Deseja cancelar o pedido #" + idPedido + "? Os itens serão devolvidos ao estoque.",
                "Cancelar Pedido");
        if (!confirmado) {
            return;
        }

        try {
            pedidoService.cancelarPedido(idPedido);
            GuiUtils.mostrarInfo(this, "Pedido #" + idPedido + " cancelado e estoque restaurado.");
            atualizarTabela();
        } catch (PedidoInvalidoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao cancelar pedido: " + e.getMessage());
        }
    }
}
