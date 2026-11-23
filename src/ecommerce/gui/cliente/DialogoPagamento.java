package ecommerce.gui.cliente;

import ecommerce.gui.GuiUtils;
import ecommerce.modelo.pagamento.BoletoBancario;
import ecommerce.modelo.pagamento.CartaoCredito;
import ecommerce.modelo.pagamento.FormaPagamento;
import ecommerce.modelo.pagamento.Pix;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/** Diálogo de escolha da forma de pagamento (Pix, Cartão com parcelas, Boleto) mostrando o total calculado. */
public class DialogoPagamento extends JDialog {

    private double subtotal;
    private double frete;
    private FormaPagamento formaEscolhida;

    private JRadioButton rbPix;
    private JRadioButton rbCartao;
    private JRadioButton rbBoleto;
    private JComboBox<Integer> comboParcelas;

    private JLabel lblSubtotal;
    private JLabel lblFrete;
    private JLabel lblCondicao;
    private JLabel lblTotal;

    public DialogoPagamento(Frame dono, double subtotal, double frete) {
        super(dono, "Finalizar Pagamento", true);
        this.subtotal = subtotal;
        this.frete = frete;
        this.formaEscolhida = null;
        montarComponentes();
        atualizarTotalExibido();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Opções de pagamento
        JPanel painelOpcoes = new JPanel(new GridBagLayout());
        painelOpcoes.setBorder(BorderFactory.createTitledBorder("Selecione a forma de pagamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rbPix = new JRadioButton("Pix (5% de desconto)", true);
        rbCartao = new JRadioButton("Cartão de Crédito (até 3x sem juros; 4x a 6x com 2% a.m.)");
        rbBoleto = new JRadioButton("Boleto Bancário (sem desconto e sem juros)");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbPix);
        grupo.add(rbCartao);
        grupo.add(rbBoleto);

        comboParcelas = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        comboParcelas.setEnabled(false);

        rbPix.addActionListener(e -> {
            comboParcelas.setEnabled(false);
            atualizarTotalExibido();
        });
        rbCartao.addActionListener(e -> {
            comboParcelas.setEnabled(true);
            atualizarTotalExibido();
        });
        rbBoleto.addActionListener(e -> {
            comboParcelas.setEnabled(false);
            atualizarTotalExibido();
        });
        comboParcelas.addActionListener(e -> atualizarTotalExibido());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelOpcoes.add(rbPix, gbc);

        gbc.gridy = 1;
        painelOpcoes.add(rbCartao, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(2, 28, 6, 8);
        painelOpcoes.add(new JLabel("Parcelas:"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(2, 4, 6, 8);
        painelOpcoes.add(comboParcelas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 8, 6, 8);
        painelOpcoes.add(rbBoleto, gbc);

        add(painelOpcoes, BorderLayout.NORTH);

        // Resumo de valores
        JPanel painelResumo = new JPanel(new GridBagLayout());
        painelResumo.setBorder(BorderFactory.createTitledBorder("Resumo de Valores"));
        GridBagConstraints gRes = new GridBagConstraints();
        gRes.insets = new Insets(4, 10, 4, 10);
        gRes.anchor = GridBagConstraints.WEST;
        gRes.fill = GridBagConstraints.HORIZONTAL;

        lblSubtotal = new JLabel(GuiUtils.formatarMoeda(subtotal));
        lblFrete = new JLabel(frete <= 0 ? "Grátis (R$ 0,00)" : GuiUtils.formatarMoeda(frete));
        lblCondicao = new JLabel("-");
        lblTotal = new JLabel("-");
        lblTotal.setFont(GuiUtils.FONTE_DESTAQUE);

        adicionarLinhaResumo(painelResumo, gRes, 0, "Subtotal dos itens:", lblSubtotal);
        adicionarLinhaResumo(painelResumo, gRes, 1, "Frete:", lblFrete);
        adicionarLinhaResumo(painelResumo, gRes, 2, "Condição:", lblCondicao);
        adicionarLinhaResumo(painelResumo, gRes, 3, "Total a pagar:", lblTotal);

        add(painelResumo, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnConfirmar = new JButton("Confirmar Pedido");

        btnCancelar.addActionListener(e -> {
            formaEscolhida = null;
            dispose();
        });
        btnConfirmar.addActionListener(e -> aoConfirmar());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnConfirmar);
        add(painelBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirmar);
        setMinimumSize(new Dimension(480, 350));
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void adicionarLinhaResumo(JPanel painel, GridBagConstraints gbc, int linha,
                                      String rotulo, JLabel valor) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painel.add(valor, gbc);
    }

    private FormaPagamento criarFormaSelecionada() {
        if (rbPix.isSelected()) {
            return new Pix();
        }
        if (rbCartao.isSelected()) {
            Integer parcelas = (Integer) comboParcelas.getSelectedItem();
            return new CartaoCredito(parcelas != null ? parcelas : 1);
        }
        return new BoletoBancario();
    }

    /** Recalcula e exibe o total ao trocar a forma/parcelas (POLIMORFISMO). */
    private void atualizarTotalExibido() {
        try {
            FormaPagamento forma = criarFormaSelecionada();
            double total = forma.calcularTotal(subtotal, frete);

            String condicao = forma.getDescricao();
            if (forma instanceof CartaoCredito) {
                CartaoCredito cartao = (CartaoCredito) forma;
                double valorParcela = cartao.calcularValorParcela(subtotal, frete);
                condicao += " — " + cartao.getParcelas() + "x de " + GuiUtils.formatarMoeda(valorParcela);
            } else if (!forma.pagamentoImediato()) {
                condicao += " (aguarda confirmação)";
            }

            lblCondicao.setText(condicao);
            lblTotal.setText(GuiUtils.formatarMoeda(total));
        } catch (RuntimeException e) {
            lblCondicao.setText("Indisponível");
            lblTotal.setText("-");
        }
    }

    private void aoConfirmar() {
        try {
            this.formaEscolhida = criarFormaSelecionada();
            dispose();
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Não foi possível selecionar a forma de pagamento: " + e.getMessage());
        }
    }

    /** @return forma escolhida ou null se o usuário cancelou. */
    public FormaPagamento getFormaEscolhida() {
        return formaEscolhida;
    }
}
