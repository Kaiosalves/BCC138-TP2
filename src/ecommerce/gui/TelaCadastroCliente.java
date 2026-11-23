package ecommerce.gui;

import ecommerce.excecao.PersistenciaException;
import ecommerce.servico.AutenticacaoService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Formulário de auto-cadastro do cliente. */
public class TelaCadastroCliente extends JDialog {

    private AutenticacaoService autenticacaoService;

    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public TelaCadastroCliente(Frame dono, AutenticacaoService autenticacaoService) {
        super(dono, "Cadastro de Cliente", true);
        this.autenticacaoService = autenticacaoService;
        montarComponentes();
    }

    private void montarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        JLabel lblTitulo = new JLabel("Criar nova conta de cliente");
        lblTitulo.setFont(GuiUtils.FONTE_SUBTITULO);
        painelTitulo.add(lblTitulo);
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNome = new JTextField(24);
        campoCpf = new JTextField(24);
        campoEmail = new JTextField(24);
        campoEndereco = new JTextField(24);
        campoLogin = new JTextField(24);
        campoSenha = new JPasswordField(24);
        campoConfirmarSenha = new JPasswordField(24);

        adicionarLinha(painelForm, gbc, 0, "Nome completo:", campoNome);
        adicionarLinha(painelForm, gbc, 1, "CPF:", campoCpf);
        adicionarLinha(painelForm, gbc, 2, "E-mail:", campoEmail);
        adicionarLinha(painelForm, gbc, 3, "Endereço:", campoEndereco);
        adicionarLinha(painelForm, gbc, 4, "Login:", campoLogin);
        adicionarLinha(painelForm, gbc, 5, "Senha:", campoSenha);
        adicionarLinha(painelForm, gbc, 6, "Confirmar senha:", campoConfirmarSenha);

        add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar");

        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> aoClicarSalvar());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSalvar);
        setMinimumSize(new Dimension(460, 360));
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gbc, int linha, String rotulo, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painel.add(campo, gbc);
    }

    /** Valida e chama o serviço; exibe erros ao usuário. */
    private void aoClicarSalvar() {
        String nome = campoNome.getText().trim();
        String cpf = campoCpf.getText().trim();
        String email = campoEmail.getText().trim();
        String endereco = campoEndereco.getText().trim();
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());

        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty()
                || endereco.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            GuiUtils.mostrarAviso(this, "Todos os campos são obrigatórios.");
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            GuiUtils.mostrarAviso(this, "As senhas informadas não conferem.");
            campoConfirmarSenha.setText("");
            campoConfirmarSenha.requestFocusInWindow();
            return;
        }

        try {
            autenticacaoService.cadastrarCliente(login, senha, nome, email, cpf, endereco);
            GuiUtils.mostrarInfo(this, "Cadastro realizado com sucesso! Você já pode fazer login.");
            dispose();
        } catch (IllegalArgumentException | PersistenciaException e) {
            GuiUtils.mostrarErro(this, e.getMessage());
        } catch (RuntimeException e) {
            GuiUtils.mostrarErro(this, "Erro ao cadastrar cliente: " + e.getMessage());
        }
    }
}
