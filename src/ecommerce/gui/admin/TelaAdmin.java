package ecommerce.gui.admin;

import ecommerce.modelo.usuario.Administrador;
import ecommerce.servico.CatalogoService;
import ecommerce.servico.PedidoService;
import ecommerce.servico.RelatorioService;
import javax.swing.JFrame;

/** Janela principal do administrador (abas: Produtos, Pedidos, Relatórios). */
public class TelaAdmin extends JFrame {

    private Administrador admin;

    public TelaAdmin(Administrador admin, CatalogoService catalogo,
                     PedidoService pedidos, RelatorioService relatorios) { throw new UnsupportedOperationException("TODO"); }

    private void montarComponentes() { throw new UnsupportedOperationException("TODO"); }
    private void sair() { throw new UnsupportedOperationException("TODO"); }
}
