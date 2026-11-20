package ecommerce.servico;

import ecommerce.excecao.CredenciaisInvalidasException;
import ecommerce.excecao.PersistenciaException;
import ecommerce.modelo.usuario.Cliente;
import ecommerce.modelo.usuario.Usuario;
import ecommerce.persistencia.RepositorioUsuarios;

/** Login e cadastro de clientes. */
public class AutenticacaoService {

    private RepositorioUsuarios repoUsuarios;

    public AutenticacaoService(RepositorioUsuarios repoUsuarios) {
        // TODO
    }

    /** Valida login/senha. @return o usuário autenticado (Administrador ou Cliente). */
    public Usuario autenticar(String login, String senha) throws CredenciaisInvalidasException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Cadastra um novo cliente (valida campos e login duplicado) e persiste. */
    public Cliente cadastrarCliente(String login, String senha, String nome, String email,
                                    String cpf, String endereco)
            throws IllegalArgumentException, PersistenciaException {
        throw new UnsupportedOperationException("TODO");
    }
}
