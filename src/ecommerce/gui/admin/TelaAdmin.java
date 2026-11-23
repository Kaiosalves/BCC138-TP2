package ecommerce.gui.admin;

import ecommerce.gui.GuiUtils;
import ecommerce.modelo.usuario.Administrador;
import ecommerce.servico.CatalogoService;
import ecommerce.servico.PedidoService;
import ecommerce.servico.RelatorioService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Janela principal do administrador (abas: Produtos, Pedidos, Relatórios). */
public class TelaAdmin extends JFrame {

    private Administrador admin;
    private CatalogoService catalogoService;
    private PedidoService pedidoService;
    private RelatorioService relatorioService;

    private PainelProdutos painelProdutos;
    private PainelPedidos painelPedidos;
    private PainelRelatorios painelRelatorios;

    public TelaAdmin(Administrador admin, CatalogoService catalogo,
                     PedidoService pedidos, RelatorioService relatorios) {
        this.admin = admin;
        this.catalogoService = catalogo;
        this.pedidoService = pedidos;
        this.relatorioService = relatorios;
        montarComponentes();
    }

    private void montarComponentes() {
        String nomeAdmin = admin != null ? admin.getNome() : "Administrador";
        setTitle("Loja Virtual — Administração (" + nomeAdmin + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Cabeçalho com identificação do administrador e botão Sair
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBorder(BorderFactory.createEmptyBorder(8, 14, 4, 14));

        JLabel lblUsuario = new JLabel("Administrador: " + nomeAdmin);
        lblUsuario.setFont(GuiUtils.FONTE_SUBTITULO);
        painelTopo.add(lblUsuario, BorderLayout.WEST);

        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> sair());
        painelSair.add(btnSair);
        painelTopo.add(painelSair, BorderLayout.EAST);

        add(painelTopo, BorderLayout.NORTH);

        // Abas administrativas
        painelProdutos = new PainelProdutos(catalogoService);
        painelPedidos = new PainelPedidos(pedidoService);
        painelRelatorios = new PainelRelatorios(relatorioService);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Produtos", painelProdutos);
        abas.addTab("Pedidos", painelPedidos);
        abas.addTab("Relatórios", painelRelatorios);

        abas.addChangeListener(e -> {
            int idx = abas.getSelectedIndex();
            if (idx == 0) {
                painelProdutos.atualizarTabela();
            } else if (idx == 1) {
                painelPedidos.atualizarTabela();
            } else if (idx == 2) {
                painelRelatorios.atualizar();
            }
        });

        add(abas, BorderLayout.CENTER);

        setMinimumSize(new Dimension(920, 600));
        setSize(1040, 680);
        setLocationRelativeTo(null);
    }

    private void sair() {
        if (GuiUtils.confirmar(this, "Deseja encerrar a sessão do administrador?", "Sair")) {
            dispose();
        }
    }
}
