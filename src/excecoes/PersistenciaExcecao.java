package excecoes;

public class PersistenciaExcecao extends Exception {
    public PersistenciaExcecao(String mensagem) {
        super(mensagem);
    }

    public PersistenciaExcecao(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}