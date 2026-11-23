package ecommerce.gui.admin;

import ecommerce.excecao.PersistenciaException;
import ecommerce.excecao.ProdutoNaoEncontradoException;
import ecommerce.gui.GuiUtils;
import ecommerce.modelo.produto.Produto;
import ecommerce.modelo.produto.ProdutoDigital;
import ecommerce.modelo.produto.ProdutoFisico;
import ecommerce.servico.CatalogoService;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** CRUD de produtos. O formulário muda conforme o tipo (físico/digital) — usa DOWNCASTING ao editar. */
public class PainelProdutos extends JPanel {

    private static final String TIPO_FISICO = "Produto Físico";
    private static final String TIPO_DIGITAL = "Produto Digital";
    private static final String CARD_FISICO = "FISICO";
    private static final String CARD_DIGITAL = "DIGITAL";

    private CatalogoService catalogo;
    private List<Produto> produtosExibidos;
    private boolean editando;

    private JTextField campoFiltro;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private JComboBox<String> comboTipo;
    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoCategoria;
    private JTextField campoPreco;
    private JTextArea campoDescricao;

    private JPanel painelEspecifico;
    private CardLayout cardEspecifico;
    private JTextField campoEstoque;
    private JTextField campoPesoKg;
    private JTextField campoTamanhoMb;
    private JTextField campoLinkDownload;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    public PainelProdutos(CatalogoService catalogo) {
        this.catalogo = catalogo;
        this.produtosExibidos = new ArrayList<>();
        montarComponentes();
        limparFormulario();
        atualizarTabela();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra de busca superior
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelBusca.add(new JLabel("Buscar por nome:"));
        campoFiltro = new JTextField(22);
        JButton btnBuscar = new JButton("Filtrar");
        JButton btnLimparBusca = new JButton("Mostrar Todos");

        btnBuscar.addActionListener(e -> atualizarTabela());
        campoFiltro.addActionListener(e -> atualizarTabela());
        btnLimparBusca.addActionListener(e -> {
            campoFiltro.setText("");
            atualizarTabela();
        });

        painelBusca.add(campoFiltro);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnLimparBusca);
        add(painelBusca, BorderLayout.NORTH);

        // Tabela de produtos à esquerda
        modeloTabela = GuiUtils.criarModeloTabela(
                "Código", "Tipo", "Nome", "Categoria", "Preço", "Estoque", "Detalhes");
        tabelaProdutos = new JTable(modeloTabela);
        GuiUtils.configurarTabela(tabelaProdutos);
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aoSelecionarLinha();
            }
        });
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setPreferredSize(new Dimension(560, 420));

        // Formulário à direita
        JPanel painelFormulario = criarPainelFormulario();
        painelFormulario.setPreferredSize(new Dimension(380, 420));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabela, painelFormulario);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);
    }

    private JPanel criarPainelFormulario() {
        JPanel container = new JPanel(new BorderLayout(8, 8));
        container.setBorder(BorderFactory.createTitledBorder("Cadastro / Edição de Produto"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboTipo = new JComboBox<>(new String[]{TIPO_FISICO, TIPO_DIGITAL});
        comboTipo.addActionListener(e -> alternarCamposPorTipo());

        campoCodigo = new JTextField(16);
        campoNome = new JTextField(16);
        campoCategoria = new JTextField(16);
        campoPreco = new JTextField(16);
        campoDescricao = new JTextArea(3, 16);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);

        int linha = 0;
        adicionarCampo(form, gbc, linha++, "Tipo:", comboTipo);
        adicionarCampo(form, gbc, linha++, "Código:", campoCodigo);
        adicionarCampo(form, gbc, linha++, "Nome:", campoNome);
        adicionarCampo(form, gbc, linha++, "Categoria:", campoCategoria);
        adicionarCampo(form, gbc, linha++, "Preço (R$):", campoPreco);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Descrição:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(campoDescricao), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        linha++;

        // Campos específicos (Físico x Digital)
        cardEspecifico = new CardLayout();
        painelEspecifico = new JPanel(cardEspecifico);
        painelEspecifico.setBorder(BorderFactory.createTitledBorder("Atributos Específicos"));

        JPanel cardFisico = new JPanel(new GridBagLayout());
        GridBagConstraints gF = new GridBagConstraints();
        gF.insets = new Insets(4, 4, 4, 4);
        gF.fill = GridBagConstraints.HORIZONTAL;
        campoEstoque = new JTextField(14);
        campoPesoKg = new JTextField(14);
        adicionarCampo(cardFisico, gF, 0, "Estoque:", campoEstoque);
        adicionarCampo(cardFisico, gF, 1, "Peso (kg):", campoPesoKg);

        JPanel cardDigital = new JPanel(new GridBagLayout());
        GridBagConstraints gD = new GridBagConstraints();
        gD.insets = new Insets(4, 4, 4, 4);
        gD.fill = GridBagConstraints.HORIZONTAL;
        campoTamanhoMb = new JTextField(14);
        campoLinkDownload = new JTextField(14);
        adicionarCampo(cardDigital, gD, 0, "Tamanho (MB):", campoTamanhoMb);
        adicionarCampo(cardDigital, gD, 1, "Link download:", campoLinkDownload);
        gD.gridx = 0;
        gD.gridy = 2;
        gD.gridwidth = 2;
        cardDigital.add(new JLabel("Estoque: Ilimitado (sem frete)"), gD);

        painelEspecifico.add(cardFisico, CARD_FISICO);
        painelEspecifico.add(cardDigital, CARD_DIGITAL);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        form.add(painelEspecifico, gbc);

        container.add(form, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> aoSalvar());
        btnExcluir.addActionListener(e -> aoExcluir());

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        container.add(painelBotoes, BorderLayout.SOUTH);

        return container;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, int linha,
                                String rotulo, java.awt.Component componente) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painel.add(componente, gbc);
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        produtosExibidos.clear();

        if (catalogo == null) {
            return;
        }

        try {
            String filtro = campoFiltro != null ? campoFiltro.getText().trim() : "";
            List<Produto> lista = filtro.isEmpty()
                    ? catalogo.listarTodos()
                    : catalogo.buscarPorNome(filtro);

            if (lista != null) {
                produtosExibidos.addAll(lista);
            }

            for (Produto p : produtosExibidos) {
                String estoqueTexto = p.temEstoqueLimitado() ? String.valueOf(p.getEstoque()) : "Ilimitado";
                String detalhes = "";
                if (p instanceof ProdutoFisico) {
                    ProdutoFisico pf = (ProdutoFisico) p;
                    detalhes = String.format("%.2f kg", pf.getPesoKg());
                } else if (p instanceof ProdutoDigital) {
                    ProdutoDigital pd = (ProdutoDigital) p;
                    detalhes = String.format("%.2f MB", pd.getTamanhoMb());
                }

                modeloTabela.addRow(new Object[]{
                        p.getCodigo(),
                        p.getTipo(),
                        p.getNome(),
                        p.getCategoria(),
                        GuiUtils.formatarMoeda(p.getPreco()),
                        estoqueTexto,
                        detalhes
                });
            }
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void aoSelecionarLinha() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0 || linha >= produtosExibidos.size()) {
            return;
        }
        Produto selecionado = produtosExibidos.get(linha);
        preencherFormulario(selecionado);
    }

    /** Preenche o formulário; se for ProdutoFisico/Digital, faz instanceof + cast para campos específicos. */
    private void preencherFormulario(Produto produto) {
        if (produto == null) {
            return;
        }
        editando = true;
        campoCodigo.setText(produto.getCodigo());
        campoCodigo.setEditable(false);
        comboTipo.setEnabled(false);

        campoNome.setText(produto.getNome());
        campoCategoria.setText(produto.getCategoria());
        campoPreco.setText(String.format("%.2f", produto.getPreco()));
        campoDescricao.setText(produto.getDescricao());

        if (produto instanceof ProdutoFisico) {
            ProdutoFisico fisico = (ProdutoFisico) produto;
            comboTipo.setSelectedItem(TIPO_FISICO);
            campoEstoque.setText(String.valueOf(fisico.getEstoque()));
            campoPesoKg.setText(String.format("%.2f", fisico.getPesoKg()));
            campoTamanhoMb.setText("");
            campoLinkDownload.setText("");
        } else if (produto instanceof ProdutoDigital) {
            ProdutoDigital digital = (ProdutoDigital) produto;
            comboTipo.setSelectedItem(TIPO_DIGITAL);
            campoTamanhoMb.setText(String.format("%.2f", digital.getTamanhoMb()));
            campoLinkDownload.setText(digital.getLinkDownload());
            campoEstoque.setText("");
            campoPesoKg.setText("");
        }
        alternarCamposPorTipo();
        btnExcluir.setEnabled(true);
    }

    /** Cria ProdutoFisico ou ProdutoDigital conforme o tipo escolhido. */
    private Produto lerFormulario() {
        String codigo = campoCodigo.getText().trim();
        String nome = campoNome.getText().trim();
        String categoria = campoCategoria.getText().trim();
        String descricao = campoDescricao.getText().trim();
        double preco = GuiUtils.parseDoubleBr(campoPreco.getText());

        if (codigo.isEmpty()) {
            throw new IllegalArgumentException("O código do produto é obrigatório.");
        }

        if (TIPO_FISICO.equals(comboTipo.getSelectedItem())) {
            int estoque = GuiUtils.parseInt(campoEstoque.getText());
            double pesoKg = GuiUtils.parseDoubleBr(campoPesoKg.getText());
            return new ProdutoFisico(codigo, nome, descricao, categoria, preco, estoque, pesoKg);
        }

        double tamanhoMb = GuiUtils.parseDoubleBr(campoTamanhoMb.getText());
        String linkDownload = campoLinkDownload.getText().trim();
        return new ProdutoDigital(codigo, nome, descricao, categoria, preco, tamanhoMb, linkDownload);
    }

    private void aoSalvar() {
        try {
            Produto produto = lerFormulario();
            if (editando) {
                catalogo.alterar(produto);
                GuiUtils.mostrarInfo(this, "Produto atualizado com sucesso!");
            } else {
                catalogo.cadastrar(produto);
                GuiUtils.mostrarInfo(this, "Produto cadastrado com sucesso!");
            }
            atualizarTabela();
            limparFormulario();
        } catch (NumberFormatException e) {
            GuiUtils.mostrarAviso(this, "Verifique os valores numéricos (preço, estoque, peso ou tamanho).");
        } catch (IllegalArgumentException | ProdutoNaoEncontradoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao salvar produto: " + e.getMessage());
        }
    }

    private void aoExcluir() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0 || linha >= produtosExibidos.size()) {
            GuiUtils.mostrarAviso(this, "Selecione um produto na tabela para excluir.");
            return;
        }
        Produto selecionado = produtosExibidos.get(linha);
        boolean confirmado = GuiUtils.confirmar(
                this,
                "Deseja realmente remover o produto \"" + selecionado.getNome() + "\" (" + selecionado.getCodigo() + ")?",
                "Confirmar Exclusão");
        if (!confirmado) {
            return;
        }

        try {
            catalogo.remover(selecionado.getCodigo());
            GuiUtils.mostrarInfo(this, "Produto removido com sucesso!");
            atualizarTabela();
            limparFormulario();
        } catch (ProdutoNaoEncontradoException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao remover produto: " + e.getMessage());
        }
    }

    private void alternarCamposPorTipo() {
        if (TIPO_DIGITAL.equals(comboTipo.getSelectedItem())) {
            cardEspecifico.show(painelEspecifico, CARD_DIGITAL);
        } else {
            cardEspecifico.show(painelEspecifico, CARD_FISICO);
        }
    }

    private void limparFormulario() {
        editando = false;
        tabelaProdutos.clearSelection();
        campoCodigo.setText("");
        campoCodigo.setEditable(true);
        comboTipo.setEnabled(true);
        comboTipo.setSelectedItem(TIPO_FISICO);
        campoNome.setText("");
        campoCategoria.setText("");
        campoPreco.setText("");
        campoDescricao.setText("");
        campoEstoque.setText("");
        campoPesoKg.setText("");
        campoTamanhoMb.setText("");
        campoLinkDownload.setText("");
        alternarCamposPorTipo();
        btnExcluir.setEnabled(false);
    }
}
