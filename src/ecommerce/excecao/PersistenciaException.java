package ecommerce.excecao;

/** Lançada em falhas de leitura/gravação dos arquivos .txt (arquivo ausente, linha corrompida...). */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensagem) {
        super(mensagem);
    }
}
