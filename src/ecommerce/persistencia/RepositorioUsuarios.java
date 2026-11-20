package ecommerce.persistencia;

import ecommerce.excecao.PersistenciaException;
import ecommerce.modelo.usuario.Usuario;
import java.util.List;
import java.util.Map;

/** Guarda os usuários em memória (Map login -> Usuario) e sincroniza com usuarios.txt. */
public class RepositorioUsuarios {

    private Map<String, Usuario> usuarios;
    private String caminhoArquivo;

    public RepositorioUsuarios(String caminhoArquivo) {
        // TODO
    }

    public void carregar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
    public void salvar() throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }

    public void adicionar(Usuario usuario) { throw new UnsupportedOperationException("TODO"); }
    /** @return o usuário ou null se não existir. */
    public Usuario buscarPorLogin(String login) { throw new UnsupportedOperationException("TODO"); }
    public boolean existe(String login) { throw new UnsupportedOperationException("TODO"); }
    public List<Usuario> listarTodos() { throw new UnsupportedOperationException("TODO"); }

    /** Converte uma linha do .txt em Administrador ou Cliente conforme o campo de perfil. */
    private Usuario linhaParaUsuario(String linha) throws PersistenciaException { throw new UnsupportedOperationException("TODO"); }
}
