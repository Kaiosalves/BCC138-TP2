package ecommerce.gui.admin;

import ecommerce.gui.GuiUtils;
import ecommerce.modelo.pedido.StatusPedido;
import ecommerce.modelo.produto.Produto;
import ecommerce.servico.RelatorioService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/** Exibe os relatórios do sistema. */
public class PainelRelatorios extends JPanel {

    private RelatorioService relatorioService;

    private JLabel lblFaturamento;
    private JSpinner spinnerLimiteEstoque;
    private JTextArea areaRelatorioGeral;
    private DefaultTableModel modeloStatus;
    private DefaultTableModel modeloVendidos;
    private DefaultTableModel modeloEstoqueBaixo;

    public PainelRelatorios(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
        montarComponentes();
        atualizar();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabeçalho com faturamento e controles
        JPanel painelTopo = new JPanel(new BorderLayout(10, 4));
        lblFaturamento = new JLabel("Faturamento Total: R$ 0,00");
        lblFaturamento.setFont(GuiUtils.FONTE_DESTAQUE);

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        painelControles.add(new JLabel("Limite estoque baixo:"));
        spinnerLimiteEstoque = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
        spinnerLimiteEstoque.addChangeListener(e -> atualizar());

        JButton btnAtualizar = new JButton("Atualizar Relatórios");
        btnAtualizar.addActionListener(e -> atualizar());

        painelControles.add(spinnerLimiteEstoque);
        painelControles.add(btnAtualizar);

        painelTopo.add(lblFaturamento, BorderLayout.WEST);
        painelTopo.add(painelControles, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // Relatório textual completo
        areaRelatorioGeral = new JTextArea();
        areaRelatorioGeral.setEditable(false);
        areaRelatorioGeral.setFont(GuiUtils.FONTE_MONOSPACE);
        areaRelatorioGeral.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scrollTexto = new JScrollPane(areaRelatorioGeral);
        scrollTexto.setBorder(BorderFactory.createTitledBorder("Relatório Geral"));
        scrollTexto.setPreferredSize(new Dimension(500, 220));

        // Tabelas analíticas inferiores
        JPanel painelTabelas = new JPanel(new GridLayout(1, 3, 10, 10));

        modeloStatus = GuiUtils.criarModeloTabela("Status", "Pedidos");
        JTable tabelaStatus = new JTable(modeloStatus);
        GuiUtils.configurarTabela(tabelaStatus);
        JScrollPane scrollStatus = new JScrollPane(tabelaStatus);
        scrollStatus.setBorder(BorderFactory.createTitledBorder("Pedidos por Status"));

        modeloVendidos = GuiUtils.criarModeloTabela("Código Produto", "Qtd. Vendida");
        JTable tabelaVendidos = new JTable(modeloVendidos);
        GuiUtils.configurarTabela(tabelaVendidos);
        JScrollPane scrollVendidos = new JScrollPane(tabelaVendidos);
        scrollVendidos.setBorder(BorderFactory.createTitledBorder("Quantidade Vendida"));

        modeloEstoqueBaixo = GuiUtils.criarModeloTabela("Código", "Nome", "Estoque");
        JTable tabelaEstoque = new JTable(modeloEstoqueBaixo);
        GuiUtils.configurarTabela(tabelaEstoque);
        JScrollPane scrollEstoque = new JScrollPane(tabelaEstoque);
        scrollEstoque.setBorder(BorderFactory.createTitledBorder("Estoque Baixo (Físicos)"));

        painelTabelas.add(scrollStatus);
        painelTabelas.add(scrollVendidos);
        painelTabelas.add(scrollEstoque);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTexto, painelTabelas);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);
    }

    public void atualizar() {
        if (relatorioService == null) {
            return;
        }

        try {
            double faturamento = relatorioService.faturamentoTotal();
            lblFaturamento.setText("Faturamento Total (não cancelados): " + GuiUtils.formatarMoeda(faturamento));

            String texto = relatorioService.gerarRelatorioGeral();
            areaRelatorioGeral.setText(texto != null ? texto : "");
            areaRelatorioGeral.setCaretPosition(0);

            modeloStatus.setRowCount(0);
            Map<StatusPedido, Integer> porStatus = relatorioService.pedidosPorStatus();
            if (porStatus != null) {
                for (Map.Entry<StatusPedido, Integer> entry : porStatus.entrySet()) {
                    modeloStatus.addRow(new Object[]{entry.getKey(), entry.getValue()});
                }
            }

            modeloVendidos.setRowCount(0);
            Map<String, Integer> vendidos = relatorioService.quantidadeVendidaPorProduto();
            if (vendidos != null) {
                for (Map.Entry<String, Integer> entry : vendidos.entrySet()) {
                    modeloVendidos.addRow(new Object[]{entry.getKey(), entry.getValue()});
                }
            }

            modeloEstoqueBaixo.setRowCount(0);
            int limite = (Integer) spinnerLimiteEstoque.getValue();
            List<Produto> baixoEstoque = relatorioService.produtosComEstoqueBaixo(limite);
            if (baixoEstoque != null) {
                for (Produto p : baixoEstoque) {
                    modeloEstoqueBaixo.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getEstoque()});
                }
            }
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao gerar relatórios: " + e.getMessage());
        }
    }
}
