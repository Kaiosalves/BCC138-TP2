package ecommerce.gui;

import ecommerce.servico.AutenticacaoService;
import javax.swing.JFrame;

/** Janela de login. Autentica e abre TelaAdmin ou TelaCliente conforme o perfil (DOWNCASTING do Usuario). */
public class TelaLogin extends JFrame {

    private AutenticacaoService autenticacaoService;
    // TODO: campos (JTextField login, JPasswordField senha, botões Entrar e Cadastrar-se)

    public TelaLogin(AutenticacaoService autenticacaoService) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    /** Chama o serviço; trata CredenciaisInvalidasException com JOptionPane. */
    private void aoClicarEntrar() { throw new UnsupportedOperationException("TODO"); }
    private void abrirCadastro() { throw new UnsupportedOperationException("TODO"); }
}
