package ecommerce.modelo.usuario;

/** Classe abstrata base dos perfis de usuário. */
public abstract class Usuario {

    private String login;
    private String senha;
    private String nome;
    private String email;

    public Usuario(String login, String senha, String nome, String email) {
        // TODO
    }

    /** @return "ADMIN" ou "CLIENTE" (usado na persistência e para decidir a tela a abrir). */
    public abstract String getPerfil();

    /** @return atributos específicos do perfil serializados para o .txt. */
    public abstract String atributosEspecificosParaTexto();

    /** @return true se a senha informada confere. */
    public boolean verificarSenha(String senhaInformada) { throw new UnsupportedOperationException("TODO"); }

    public String getLogin() { throw new UnsupportedOperationException("TODO"); }
    public String getSenha() { throw new UnsupportedOperationException("TODO"); }
    public void setSenha(String senha) { throw new UnsupportedOperationException("TODO"); }
    public String getNome() { throw new UnsupportedOperationException("TODO"); }
    public void setNome(String nome) { throw new UnsupportedOperationException("TODO"); }
    public String getEmail() { throw new UnsupportedOperationException("TODO"); }
    public void setEmail(String email) { throw new UnsupportedOperationException("TODO"); }

    public String paraLinhaTexto() { throw new UnsupportedOperationException("TODO"); }

    @Override
    public String toString() { throw new UnsupportedOperationException("TODO"); }
}
