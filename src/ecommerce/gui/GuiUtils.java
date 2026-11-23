package ecommerce.gui;

import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * Utilitários compartilhados pelas telas e painéis Swing do módulo GUI.
 */
public final class GuiUtils {

    private static final Locale LOCALE_PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONTE_DESTAQUE = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONTE_MONOSPACE = new Font("Monospaced", Font.PLAIN, 13);

    private GuiUtils() {
    }

    /** Aplica um Look and Feel mais moderno (Nimbus, se disponível) sem interromper a execução em caso de falha. */
    public static void aplicarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception ignored) {
            // Mantém o Look and Feel padrão do sistema/Swing
        }
    }

    /** Formata valores monetários no padrão brasileiro (R$ 0,00). */
    public static String formatarMoeda(double valor) {
        return NumberFormat.getCurrencyInstance(LOCALE_PT_BR).format(valor);
    }

    /** Formata data e hora no padrão dd/MM/yyyy HH:mm. */
    public static String formatarDataHora(LocalDateTime data) {
        return data == null ? "-" : data.format(FORMATO_DATA_HORA);
    }

    /** Converte texto numérico aceitando vírgula ou ponto como separador decimal. */
    public static double parseDoubleBr(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe um valor numérico válido.");
        }
        String normalizado = texto.trim().replace(",", ".");
        return Double.parseDouble(normalizado);
    }

    /** Converte texto inteiro removendo espaços extras. */
    public static int parseInt(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe um número inteiro válido.");
        }
        return Integer.parseInt(texto.trim());
    }

    /** Cria um DefaultTableModel somente leitura para exibição em JTable. */
    public static DefaultTableModel criarModeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /** Configura seleção única e aparência padrão de uma JTable. */
    public static void configurarTabela(JTable tabela) {
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setFillsViewportHeight(true);
    }

    public static void mostrarErro(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarAviso(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarInfo(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component pai, String mensagem, String titulo) {
        int resposta = JOptionPane.showConfirmDialog(
                pai, mensagem, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }
}
