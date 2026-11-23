package ecommerce.gui.cliente;

import ecommerce.excecao.EstoqueInsuficienteException;
import ecommerce.excecao.PedidoInvalidoException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.excecao.ProdutoNaoEncontradoException;
import ecommerce.gui.GuiUtils;
import ecommerce.modelo.Carrinho;
import ecommerce.modelo.pagamento.FormaPagamento;
import ecommerce.modelo.pedido.ItemPedido;
import ecommerce.modelo.pedido.Pedido;
import ecommerce.modelo.produto.ProdutoFisico;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.servico.PedidoService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/** Mostra o carrinho, permite alterar quantidades, remover itens e finalizar a compra. */
public class PainelCarrinho extends JPanel {

    private Carrinho carrinho;
    private Cliente cliente;
    private PedidoService pedidoService;
    private Runnable aoAtualizarEstado;

    private List<ItemPedido> itensExibidos;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;
    private JSpinner spinnerQuantidade;

    private JLabel lblSubtotal;
    private JLabel lblFrete;
    private JLabel lblTotalEstimado;
    private JButton btnFinalizar;

    public PainelCarrinho(Carrinho carrinho, Cliente cliente, PedidoService pedidoService) {
        this(carrinho, cliente, pedidoService, null);
    }

    public PainelCarrinho(Carrinho carrinho, Cliente cliente, PedidoService pedidoService,
                          Runnable aoAtualizarEstado) {
        this.carrinho = carrinho;
        this.cliente = cliente;
        this.pedidoService = pedidoService;
        this.aoAtualizarEstado = aoAtualizarEstado;
        this.itensExibidos = new ArrayList<>();
        montarComponentes();
        atualizarTabela();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela dos itens no carrinho
        modeloTabela = GuiUtils.criarModeloTabela(
                "Código", "Produto", "Tipo", "Preço Unit.", "Quantidade", "Subtotal");
        tabelaItens = new JTable(modeloTabela);
        GuiUtils.configurarTabela(tabelaItens);
        tabelaItens.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ItemPedido item = getItemSelecionado();
                if (item != null) {
                    spinnerQuantidade.setValue(item.getQuantidade());
                }
            }
        });
        add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        // Painel inferior: edição de itens + resumo/finalização
        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));

        JPanel painelEdicao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        painelEdicao.setBorder(BorderFactory.createTitledBorder("Gerenciar Item Selecionado"));
        painelEdicao.add(new JLabel("Quantidade:"));
        spinnerQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        JButton btnAlterarQtd = new JButton("Alterar Quantidade");
        JButton btnRemover = new JButton("Remover Item");
        JButton btnLimpar = new JButton("Limpar Carrinho");

        btnAlterarQtd.addActionListener(e -> aoAlterarQuantidade());
        btnRemover.addActionListener(e -> aoRemoverItem());
        btnLimpar.addActionListener(e -> aoLimparCarrinho());

        painelEdicao.add(spinnerQuantidade);
        painelEdicao.add(btnAlterarQtd);
        painelEdicao.add(btnRemover);
        painelEdicao.add(btnLimpar);

        // Resumo de valores e botão finalizar
        JPanel painelCheckout = new JPanel(new BorderLayout(12, 8));
        painelCheckout.setBorder(BorderFactory.createTitledBorder("Resumo da Compra"));

        JPanel painelValores = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.anchor = GridBagConstraints.WEST;

        lblSubtotal = new JLabel("R$ 0,00");
        lblFrete = new JLabel("R$ 0,00");
        lblTotalEstimado = new JLabel("R$ 0,00");
        lblTotalEstimado.setFont(GuiUtils.FONTE_DESTAQUE);

        adicionarLinhaValor(painelValores, gbc, 0, "Subtotal:", lblSubtotal);
        adicionarLinhaValor(painelValores, gbc, 1, "Frete estimado:", lblFrete);
        adicionarLinhaValor(painelValores, gbc, 2, "Total estimado:", lblTotalEstimado);

        JPanel painelBtnFinalizar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setFont(GuiUtils.FONTE_SUBTITULO);
        btnFinalizar.addActionListener(e -> aoFinalizarCompra());
        painelBtnFinalizar.add(btnFinalizar);

        painelCheckout.add(painelValores, BorderLayout.WEST);
        painelCheckout.add(painelBtnFinalizar, BorderLayout.EAST);

        painelInferior.add(painelEdicao, BorderLayout.NORTH);
        painelInferior.add(painelCheckout, BorderLayout.SOUTH);
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void adicionarLinhaValor(JPanel painel, GridBagConstraints gbc, int linha,
                                     String rotulo, JLabel valor) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        painel.add(valor, gbc);
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        itensExibidos.clear();

        if (carrinho == null) {
            atualizarResumoValores(0.0, 0.0);
            return;
        }

        try {
            List<ItemPedido> itens = carrinho.getItens();
            if (itens != null) {
                itensExibidos.addAll(itens);
            }

            for (ItemPedido item : itensExibidos) {
                modeloTabela.addRow(new Object[]{
                        item.getProduto().getCodigo(),
                        item.getProduto().getNome(),
                        item.getProduto().getTipo(),
                        GuiUtils.formatarMoeda(item.getPrecoUnitario()),
                        item.getQuantidade(),
                        GuiUtils.formatarMoeda(item.calcularSubtotal())
                });
            }

            double subtotal = carrinho.calcularSubtotal();
            double frete = calcularFreteEstimado(subtotal);
            atualizarResumoValores(subtotal, frete);
            btnFinalizar.setEnabled(!carrinho.estaVazio());
        } catch (RuntimeException e) {
            atualizarResumoValores(0.0, 0.0);
            btnFinalizar.setEnabled(false);
        }

        if (aoAtualizarEstado != null) {
            aoAtualizarEstado.run();
        }
    }

    private void atualizarResumoValores(double subtotal, double frete) {
        lblSubtotal.setText(GuiUtils.formatarMoeda(subtotal));
        if (carrinho == null || carrinho.estaVazio()) {
            lblFrete.setText(GuiUtils.formatarMoeda(0.0));
        } else if (frete <= 0.0) {
            lblFrete.setText("Grátis (R$ 0,00)");
        } else {
            lblFrete.setText(GuiUtils.formatarMoeda(frete));
        }
        lblTotalEstimado.setText(GuiUtils.formatarMoeda(subtotal + frete));
    }

    /**
     * Calcula o frete estimado segundo as regras de negócio:
     * grátis se subtotal >= R$ 300,00 ou se houver apenas produtos digitais;
     * caso contrário, R$ 5,00 fixos + R$ 2,00/kg dos produtos físicos.
     */
    private double calcularFreteEstimado(double subtotal) {
        if (carrinho == null || carrinho.estaVazio() || subtotal >= 300.0) {
            return 0.0;
        }
        boolean exigeEntrega = false;
        double variavel = 0.0;
        for (ItemPedido item : itensExibidos) {
            if (item.getProduto().exigeEntrega()) {
                exigeEntrega = true;
                if (item.getProduto() instanceof ProdutoFisico) {
                    ProdutoFisico pf = (ProdutoFisico) item.getProduto();
                    variavel += pf.getPesoKg() * 2.0 * item.getQuantidade();
                }
            }
        }
        return exigeEntrega ? (5.0 + variavel) : 0.0;
    }

    private ItemPedido getItemSelecionado() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0 || linha >= itensExibidos.size()) {
            return null;
        }
        return itensExibidos.get(linha);
    }

    private void aoAlterarQuantidade() {
        ItemPedido item = getItemSelecionado();
        if (item == null) {
            GuiUtils.mostrarAviso(this, "Selecione um item do carrinho para alterar a quantidade.");
            return;
        }

        int novaQuantidade = (Integer) spinnerQuantidade.getValue();
        try {
            carrinho.alterarQuantidade(item.getProduto().getCodigo(), novaQuantidade);
            atualizarTabela();
        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException | IllegalArgumentException e) {
            GuiUtils.mostrarAviso(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao alterar quantidade: " + e.getMessage());
        }
    }

    private void aoRemoverItem() {
        ItemPedido item = getItemSelecionado();
        if (item == null) {
            GuiUtils.mostrarAviso(this, "Selecione um item do carrinho para remover.");
            return;
        }

        try {
            carrinho.remover(item.getProduto().getCodigo());
            atualizarTabela();
        } catch (ProdutoNaoEncontradoException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao remover item: " + e.getMessage());
        }
    }

    private void aoLimparCarrinho() {
        if (carrinho == null || carrinho.estaVazio()) {
            return;
        }
        if (GuiUtils.confirmar(this, "Deseja remover todos os itens do carrinho?", "Limpar Carrinho")) {
            carrinho.limpar();
            atualizarTabela();
        }
    }

    /** Abre o DialogoPagamento e, se confirmado, chama PedidoService.finalizarPedido. */
    private void aoFinalizarCompra() {
        if (carrinho == null || carrinho.estaVazio()) {
            GuiUtils.mostrarAviso(this, "Seu carrinho está vazio.");
            return;
        }

        double subtotal = carrinho.calcularSubtotal();
        double frete = calcularFreteEstimado(subtotal);

        Window janela = SwingUtilities.getWindowAncestor(this);
        Frame dono = (janela instanceof Frame) ? (Frame) janela : null;

        DialogoPagamento dialogo = new DialogoPagamento(dono, subtotal, frete);
        dialogo.setVisible(true);

        FormaPagamento forma = dialogo.getFormaEscolhida();
        if (forma == null) {
            return;
        }

        try {
            Pedido pedido = pedidoService.finalizarPedido(cliente, carrinho, forma);
            atualizarTabela();
            exibirResumoPedidoFinalizado(pedido);
        } catch (PedidoInvalidoException | EstoqueInsuficienteException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao finalizar compra: " + e.getMessage());
        }
    }

    private void exibirResumoPedidoFinalizado(Pedido pedido) {
        JTextArea area = new JTextArea(pedido.gerarResumo());
        area.setEditable(false);
        area.setFont(GuiUtils.FONTE_MONOSPACE);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 280));
        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Pedido #" + pedido.getId() + " Finalizado com Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
