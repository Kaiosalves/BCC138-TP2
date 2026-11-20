package ecommerce.modelo.usuario;

/** Cliente da loja: realiza o próprio cadastro, monta o carrinho e finaliza pedidos. */
public class Cliente extends Usuario {

    private String cpf;
    private String endereco;

    public Cliente(String login, String senha, String nome, String email,
                   String cpf, String endereco) {
        super(login, senha, nome, email);
        // TODO
    }

    public String getCpf() { throw new UnsupportedOperationException("TODO"); }
    public void setCpf(String cpf) { throw new UnsupportedOperationException("TODO"); }
    public String getEndereco() { throw new UnsupportedOperationException("TODO"); }
    public void setEndereco(String endereco) { throw new UnsupportedOperationException("TODO"); }

    @Override public String getPerfil() { throw new UnsupportedOperationException("TODO"); }
    @Override public String atributosEspecificosParaTexto() { throw new UnsupportedOperationException("TODO"); }
}
