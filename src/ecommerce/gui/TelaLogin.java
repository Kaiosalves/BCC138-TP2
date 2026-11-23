package ecommerce.gui;

import ecommerce.excecao.CredenciaisInvalidasException;
import ecommerce.gui.admin.TelaAdmin;
import ecommerce.gui.cliente.TelaCliente;
import ecommerce.modelo.usuario.Administrador;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.modelo.usuario.Usuario;
import ecommerce.servico.AutenticacaoService;
import ecommerce.servico.CatalogoService;
import ecommerce.servico.PedidoService;
import ecommerce.servico.RelatorioService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/** Janela de login. Autentica e abre TelaAdmin ou TelaCliente conforme o perfil (DOWNCASTING do Usuario). */
public class TelaLogin extends JFrame {

    private AutenticacaoService autenticacaoService;
    private CatalogoService catalogoService;
    private PedidoService pedidoService;
    private RelatorioService relatorioService;

    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JButton btnEntrar;
    private JButton btnCadastrar;

    public TelaLogin(AutenticacaoService autenticacaoService) {
        this(autenticacaoService, null, null, null);
    }

    public TelaLogin(AutenticacaoService autenticacaoService,
                     CatalogoService catalogoService,
                     PedidoService pedidoService,
                     RelatorioService relatorioService) {
        this.autenticacaoService = autenticacaoService;
        this.catalogoService = catalogoService;
        this.pedidoService = pedidoService;
        this.relatorioService = relatorioService;
        GuiUtils.aplicarLookAndFeel();
        montarComponentes();
    }

    private void montarComponentes() {
        setTitle("Loja Virtual — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel painelCabecalho = new JPanel(new BorderLayout());
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(16, 20, 4, 20));
        JLabel lblTitulo = new JLabel("Sistema de E-commerce", SwingConstants.CENTER);
        lblTitulo.setFont(GuiUtils.FONTE_TITULO);
        JLabel lblSubtitulo = new JLabel("Informe suas credenciais para acessar", SwingConstants.CENTER);
        painelCabecalho.add(lblTitulo, BorderLayout.NORTH);
        painelCabecalho.add(lblSubtitulo, BorderLayout.SOUTH);
        add(painelCabecalho, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoLogin = new JTextField(20);
        campoSenha = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        painelForm.add(new JLabel("Login:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painelForm.add(campoLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        painelForm.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painelForm.add(campoSenha, gbc);

        add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnEntrar = new JButton("Entrar");
        btnCadastrar = new JButton("Cadastrar-se");

        btnEntrar.addActionListener(e -> aoClicarEntrar());
        btnCadastrar.addActionListener(e -> abrirCadastro());

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnCadastrar);
        add(painelBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnEntrar);
        setMinimumSize(new Dimension(400, 250));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /** Chama o serviço; trata CredenciaisInvalidasException com JOptionPane. */
    private void aoClicarEntrar() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            GuiUtils.mostrarAviso(this, "Preencha login e senha para continuar.");
            return;
        }
        if (autenticacaoService == null) {
            GuiUtils.mostrarErro(this, "Serviço de autenticação não configurado.");
            return;
        }

        try {
            Usuario usuario = autenticacaoService.autenticar(login, senha);

            if (usuario instanceof Administrador) {
                if (catalogoService == null || pedidoService == null || relatorioService == null) {
                    GuiUtils.mostrarErro(this, "Serviços administrativos não configurados.");
                    return;
                }
                Administrador admin = (Administrador) usuario;
                abrirJanelaPerfil(new TelaAdmin(admin, catalogoService, pedidoService, relatorioService));
            } else if (usuario instanceof Cliente) {
                if (catalogoService == null || pedidoService == null) {
                    GuiUtils.mostrarErro(this, "Serviços do cliente não configurados.");
                    return;
                }
                Cliente cliente = (Cliente) usuario;
                abrirJanelaPerfil(new TelaCliente(cliente, catalogoService, pedidoService));
            } else {
                GuiUtils.mostrarErro(this, "Perfil de usuário desconhecido.");
            }
        } catch (CredenciaisInvalidasException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
            campoSenha.setText("");
            campoSenha.requestFocusInWindow();
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao autenticar: " + e.getMessage());
        }
    }

    private void abrirJanelaPerfil(JFrame telaDestino) {
        campoSenha.setText("");
        setVisible(false);
        telaDestino.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(true);
                campoLogin.requestFocusInWindow();
            }
        });
        telaDestino.setVisible(true);
    }

    private void abrirCadastro() {
        if (autenticacaoService == null) {
            GuiUtils.mostrarErro(this, "Serviço de autenticação não configurado.");
            return;
        }
        new TelaCadastroCliente(this, autenticacaoService).setVisible(true);
    }
}
