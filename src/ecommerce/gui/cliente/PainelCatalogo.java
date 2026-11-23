package ecommerce.gui.cliente;

import ecommerce.excecao.EstoqueInsuficienteException;
import ecommerce.gui.GuiUtils;
import ecommerce.modelo.Carrinho;
import ecommerce.modelo.produto.Produto;
import ecommerce.modelo.produto.ProdutoDigital;
import ecommerce.modelo.produto.ProdutoFisico;
import ecommerce.servico.CatalogoService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/** Lista os produtos disponíveis e permite adicionar ao carrinho. */
public class PainelCatalogo extends JPanel {

    private static final String TODAS_CATEGORIAS = "Todas";

    private CatalogoService catalogo;
    private Carrinho carrinho;
    private Runnable aoAlterarCarrinho;
    private List<Produto> produtosExibidos;

    private JTextField campoBusca;
    private JComboBox<String> comboCategoria;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private JLabel lblCodigo;
    private JLabel lblNome;
    private JLabel lblTipo;
    private JLabel lblCategoria;
    private JLabel lblPreco;
    private JLabel lblDisponibilidade;
    private JLabel lblInfoEspecifica;
    private JTextArea areaDescricao;
    private JSpinner spinnerQuantidade;
    private JButton btnAdicionar;

    public PainelCatalogo(CatalogoService catalogo, Carrinho carrinho) {
        this(catalogo, carrinho, null);
    }

    public PainelCatalogo(CatalogoService catalogo, Carrinho carrinho, Runnable aoAlterarCarrinho) {
        this.catalogo = catalogo;
        this.carrinho = carrinho;
        this.aoAlterarCarrinho = aoAlterarCarrinho;
        this.produtosExibidos = new ArrayList<>();
        montarComponentes();
        atualizarLista();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra de busca e categoria
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelFiltro.add(new JLabel("Buscar:"));
        campoBusca = new JTextField(18);
        painelFiltro.add(campoBusca);

        painelFiltro.add(new JLabel("Categoria:"));
        comboCategoria = new JComboBox<>(new String[]{TODAS_CATEGORIAS});
        painelFiltro.add(comboCategoria);

        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpar = new JButton("Limpar");

        btnFiltrar.addActionListener(e -> atualizarLista());
        campoBusca.addActionListener(e -> atualizarLista());
        comboCategoria.addActionListener(e -> atualizarLista());
        btnLimpar.addActionListener(e -> {
            campoBusca.setText("");
            comboCategoria.setSelectedItem(TODAS_CATEGORIAS);
            atualizarLista();
        });

        painelFiltro.add(btnFiltrar);
        painelFiltro.add(btnLimpar);
        add(painelFiltro, BorderLayout.NORTH);

        // Tabela de produtos disponíveis
        modeloTabela = GuiUtils.criarModeloTabela(
                "Código", "Tipo", "Nome", "Categoria", "Preço", "Disponibilidade");
        tabelaProdutos = new JTable(modeloTabela);
        GuiUtils.configurarTabela(tabelaProdutos);
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalhes();
            }
        });
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setPreferredSize(new Dimension(540, 420));

        // Painel de detalhes à direita
        JPanel painelDetalhes = criarPainelDetalhes();
        painelDetalhes.setPreferredSize(new Dimension(340, 420));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabela, painelDetalhes);
        split.setResizeWeight(0.62);
        add(split, BorderLayout.CENTER);
    }

    private JPanel criarPainelDetalhes() {
        JPanel container = new JPanel(new BorderLayout(8, 8));
        container.setBorder(BorderFactory.createTitledBorder("Detalhes do Produto"));

        JPanel info = new JPanel(new GridBagLayout());
        info.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblCodigo = new JLabel("-");
        lblNome = new JLabel("-");
        lblNome.setFont(GuiUtils.FONTE_SUBTITULO);
        lblTipo = new JLabel("-");
        lblCategoria = new JLabel("-");
        lblPreco = new JLabel("-");
        lblPreco.setFont(GuiUtils.FONTE_DESTAQUE);
        lblDisponibilidade = new JLabel("-");
        lblInfoEspecifica = new JLabel("-");

        areaDescricao = new JTextArea(4, 20);
        areaDescricao.setEditable(false);
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);

        int linha = 0;
        adicionarCampoDetalhe(info, gbc, linha++, "Nome:", lblNome);
        adicionarCampoDetalhe(info, gbc, linha++, "Código:", lblCodigo);
        adicionarCampoDetalhe(info, gbc, linha++, "Tipo:", lblTipo);
        adicionarCampoDetalhe(info, gbc, linha++, "Categoria:", lblCategoria);
        adicionarCampoDetalhe(info, gbc, linha++, "Preço:", lblPreco);
        adicionarCampoDetalhe(info, gbc, linha++, "Disponibilidade:", lblDisponibilidade);
        adicionarCampoDetalhe(info, gbc, linha++, "Especificação:", lblInfoEspecifica);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        info.add(new JLabel("Descrição:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        info.add(new JScrollPane(areaDescricao), gbc);

        container.add(info, BorderLayout.CENTER);

        // Barra inferior: quantidade + adicionar ao carrinho
        JPanel painelCompra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        painelCompra.add(new JLabel("Quantidade:"));
        spinnerQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        btnAdicionar = new JButton("Adicionar ao Carrinho");
        btnAdicionar.setEnabled(false);
        btnAdicionar.addActionListener(e -> aoAdicionarAoCarrinho());

        painelCompra.add(spinnerQuantidade);
        painelCompra.add(btnAdicionar);
        container.add(painelCompra, BorderLayout.SOUTH);

        return container;
    }

    private void adicionarCampoDetalhe(JPanel painel, GridBagConstraints gbc, int linha,
                                       String rotulo, JLabel valor) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painel.add(valor, gbc);
    }

    public void atualizarLista() {
        modeloTabela.setRowCount(0);
        produtosExibidos.clear();
        limparDetalhes();

        if (catalogo == null) {
            return;
        }

        try {
            List<Produto> disponiveis = catalogo.listarDisponiveis();
            if (disponiveis == null) {
                return;
            }

            atualizarComboCategorias(disponiveis);

            String termo = campoBusca != null ? campoBusca.getText().trim().toLowerCase() : "";
            String categoriaSelecionada = comboCategoria != null
                    ? (String) comboCategoria.getSelectedItem()
                    : TODAS_CATEGORIAS;

            for (Produto p : disponiveis) {
                boolean casaNome = termo.isEmpty()
                        || p.getNome().toLowerCase().contains(termo)
                        || p.getCodigo().toLowerCase().contains(termo);
                boolean casaCategoria = categoriaSelecionada == null
                        || TODAS_CATEGORIAS.equals(categoriaSelecionada)
                        || p.getCategoria().equalsIgnoreCase(categoriaSelecionada);

                if (casaNome && casaCategoria) {
                    produtosExibidos.add(p);
                    String disp = p.temEstoqueLimitado()
                            ? p.getEstoque() + " un."
                            : "Ilimitado";
                    modeloTabela.addRow(new Object[]{
                            p.getCodigo(),
                            p.getTipo(),
                            p.getNome(),
                            p.getCategoria(),
                            GuiUtils.formatarMoeda(p.getPreco()),
                            disp
                    });
                }
            }
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao carregar catálogo: " + e.getMessage());
        }
    }

    private void atualizarComboCategorias(List<Produto> disponiveis) {
        if (comboCategoria == null) {
            return;
        }
        String selecionada = (String) comboCategoria.getSelectedItem();
        Set<String> categorias = new LinkedHashSet<>();
        categorias.add(TODAS_CATEGORIAS);
        for (Produto p : disponiveis) {
            if (p.getCategoria() != null && !p.getCategoria().trim().isEmpty()) {
                categorias.add(p.getCategoria().trim());
            }
        }

        // Evita disparar ActionListener durante a reconstrução dos itens
        var listeners = comboCategoria.getActionListeners();
        for (var l : listeners) {
            comboCategoria.removeActionListener(l);
        }
        comboCategoria.removeAllItems();
        for (String cat : categorias) {
            comboCategoria.addItem(cat);
        }
        if (selecionada != null && categorias.contains(selecionada)) {
            comboCategoria.setSelectedItem(selecionada);
        } else {
            comboCategoria.setSelectedItem(TODAS_CATEGORIAS);
        }
        for (var l : listeners) {
            comboCategoria.addActionListener(l);
        }
    }

    private Produto getProdutoSelecionado() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0 || linha >= produtosExibidos.size()) {
            return null;
        }
        return produtosExibidos.get(linha);
    }

    /** Exibe detalhes; faz downcasting para mostrar peso (físico) ou tamanho/link (digital). */
    private void mostrarDetalhes() {
        Produto produto = getProdutoSelecionado();
        if (produto == null) {
            limparDetalhes();
            return;
        }

        lblNome.setText(produto.getNome());
        lblCodigo.setText(produto.getCodigo());
        lblTipo.setText(produto.getTipo());
        lblCategoria.setText(produto.getCategoria());
        lblPreco.setText(GuiUtils.formatarMoeda(produto.getPreco()));
        lblDisponibilidade.setText(produto.temEstoqueLimitado()
                ? produto.getEstoque() + " em estoque"
                : "Estoque ilimitado");
        areaDescricao.setText(produto.getDescricao());
        areaDescricao.setCaretPosition(0);

        if (produto instanceof ProdutoFisico) {
            ProdutoFisico fisico = (ProdutoFisico) produto;
            lblInfoEspecifica.setText(String.format(
                    "<html>Peso: %.2f kg<br/>Frete: R$ 5,00 + R$ 2,00/kg (grátis &ge; R$ 300,00)</html>",
                    fisico.getPesoKg()));
        } else if (produto instanceof ProdutoDigital) {
            ProdutoDigital digital = (ProdutoDigital) produto;
            lblInfoEspecifica.setText(String.format(
                    "<html>Tamanho: %.2f MB<br/>Download: %s<br/>Sem cobrança de frete</html>",
                    digital.getTamanhoMb(), digital.getLinkDownload()));
        } else {
            lblInfoEspecifica.setText("-");
        }

        btnAdicionar.setEnabled(true);
    }

    private void limparDetalhes() {
        lblNome.setText("Selecione um produto");
        lblCodigo.setText("-");
        lblTipo.setText("-");
        lblCategoria.setText("-");
        lblPreco.setText("-");
        lblDisponibilidade.setText("-");
        lblInfoEspecifica.setText("-");
        areaDescricao.setText("");
        btnAdicionar.setEnabled(false);
    }

    /** Trata EstoqueInsuficienteException com mensagem ao usuário. */
    private void aoAdicionarAoCarrinho() {
        Produto selecionado = getProdutoSelecionado();
        if (selecionado == null) {
            GuiUtils.mostrarAviso(this, "Selecione um produto para adicionar ao carrinho.");
            return;
        }

        int quantidade = (Integer) spinnerQuantidade.getValue();
        try {
            carrinho.adicionar(selecionado, quantidade);
            spinnerQuantidade.setValue(1);
            if (aoAlterarCarrinho != null) {
                aoAlterarCarrinho.run();
            }
            GuiUtils.mostrarInfo(this, quantidade + "x \"" + selecionado.getNome() + "\" adicionado ao carrinho!");
        } catch (EstoqueInsuficienteException e) {
            GuiUtils.mostrarAviso(this, e.getMessage());
        } catch (IllegalArgumentException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao adicionar ao carrinho: " + e.getMessage());
        }
    }
}
