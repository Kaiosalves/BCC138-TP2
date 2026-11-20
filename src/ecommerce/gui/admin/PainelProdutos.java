package ecommerce.gui.admin;

import ecommerce.modelo.produto.Produto;
import ecommerce.servico.CatalogoService;
import javax.swing.JPanel;

/** CRUD de produtos. O formulário muda conforme o tipo (físico/digital) — usa DOWNCASTING ao editar. */
public class PainelProdutos extends JPanel {

    private CatalogoService catalogo;
    // TODO: JTable/ DefaultTableModel, campos, botões Novo/Salvar/Excluir

    public PainelProdutos(CatalogoService catalogo) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    public void atualizarTabela() { throw new UnsupportedOperationException("TODO"); }
    /** Preenche o formulário; se for ProdutoFisico/Digital, faz instanceof + cast para campos específicos. */
    private void preencherFormulario(Produto produto) { throw new UnsupportedOperationException("TODO"); }
    /** Cria ProdutoFisico ou ProdutoDigital conforme o tipo escolhido. */
    private Produto lerFormulario() { throw new UnsupportedOperationException("TODO"); }
    private void aoSalvar() { throw new UnsupportedOperationException("TODO"); }
    private void aoExcluir() { throw new UnsupportedOperationException("TODO"); }
    private void alternarCamposPorTipo() { throw new UnsupportedOperationException("TODO"); }
}
