package controle;

import excecoes.MatriculaInvalidaExcecao;
import excecoes.PersistenciaExcecao;
import modelo.Bilhete;
import modelo.ValidadorMatricula;
import dao.BilheteDAO;

import java.time.LocalDate;
import java.util.List;

public class HistoricoControle {

    private final BilheteDAO bilheteDAO;

    public HistoricoControle(BilheteDAO bilheteDAO) {
        this.bilheteDAO = bilheteDAO;
    }

    public List<Bilhete> listarTodos() throws PersistenciaExcecao {
        return bilheteDAO.listarTodos();
    }

    public List<Bilhete> listarAtivos() throws PersistenciaExcecao {
        return bilheteDAO.listarAtivos();
    }

    public List<Bilhete> pesquisarPorMatricula(String matricula)
            throws MatriculaInvalidaExcecao, PersistenciaExcecao {
        return bilheteDAO.pesquisarPorMatricula(ValidadorMatricula.validar(matricula));
    }

    public List<Bilhete> pesquisarPorTexto(String texto) throws PersistenciaExcecao {
        if (texto == null || texto.trim().isEmpty()) {
            return bilheteDAO.listarTodos();
        }
        return bilheteDAO.pesquisarPorTexto(texto.trim());
    }

    public List<Bilhete> listarPorData(LocalDate data) throws PersistenciaExcecao {
        return bilheteDAO.listarPorData(data);
    }

    public double totalFaturado(LocalDate data) throws PersistenciaExcecao {
        return bilheteDAO.totalFaturado(data);
    }
    
    public List<Bilhete> filtrar(String texto, String tipoVeiculo, String estado, LocalDate data) throws PersistenciaExcecao {
        return bilheteDAO.filtrar(texto, tipoVeiculo, estado, data);
    }
    
    public Bilhete procurarAtivoPorVaga(int numeroVaga) throws PersistenciaExcecao {
        return bilheteDAO.procurarAtivoPorVaga(numeroVaga);
    }
}