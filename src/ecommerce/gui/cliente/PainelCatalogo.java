package ecommerce.gui.cliente;

import ecommerce.modelo.Carrinho;
import ecommerce.servico.CatalogoService;
import javax.swing.JPanel;

/** Lista os produtos disponíveis e permite adicionar ao carrinho. */
public class PainelCatalogo extends JPanel {

    private CatalogoService catalogo;
    private Carrinho carrinho;

    public PainelCatalogo(CatalogoService catalogo, Carrinho carrinho) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    public void atualizarLista() { throw new UnsupportedOperationException("TODO"); }
    /** Exibe detalhes; faz downcasting para mostrar peso (físico) ou tamanho/link (digital). */
    private void mostrarDetalhes() { throw new UnsupportedOperationException("TODO"); }
    /** Trata EstoqueInsuficienteException com mensagem ao usuário. */
    private void aoAdicionarAoCarrinho() { throw new UnsupportedOperationException("TODO"); }
}
