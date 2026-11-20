package ecommerce.modelo.usuario;

/** Usuário administrador (pré-cadastrado). */
public class Administrador extends Usuario {

    public Administrador(String login, String senha, String nome, String email) {
        super(login, senha, nome, email);
    }

    @Override public String getPerfil() { throw new UnsupportedOperationException("TODO"); }
    @Override public String atributosEspecificosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
