package controle;

import excecoes.PersistenciaExcecao;
import excecoes.VagasEsgotadasExcecao;
import modelo.TipoVaga;
import modelo.Vaga;
import modelo.Veiculo;
import dao.VagaDAO;

import java.util.List;

public class VagaControle {

    private final VagaDAO vagaDAO;

    public VagaControle(VagaDAO vagaDAO) {
        this.vagaDAO = vagaDAO;
    }

    public Vaga atribuirVaga(Veiculo v) throws VagasEsgotadasExcecao, PersistenciaExcecao {
        Vaga vaga = vagaDAO.procurarLivrePorTipo(v.getTipo());
        if (vaga == null) {
            throw new VagasEsgotadasExcecao(
                "Estacionamento cheio para o tipo " + v.getTipo().getDescricao());
        }
        vaga.ocupar();
        vagaDAO.atualizarEstado(vaga.getNumero(), true);
        return vaga;
    }

    public void libertarVaga(int numero) throws PersistenciaExcecao {
        vagaDAO.atualizarEstado(numero, false);
    }

    public List<Vaga> listarTodas() throws PersistenciaExcecao {
        return vagaDAO.listarTodas();
    }

    public List<Vaga> listarLivres() throws PersistenciaExcecao {
        return vagaDAO.listarLivres();
    }

    public List<Vaga> listarOcupadas() throws PersistenciaExcecao {
        return vagaDAO.listarOcupadas();
    }

    public int contarLivres(TipoVaga tipo) throws PersistenciaExcecao {
        return vagaDAO.contarLivresPorTipo(tipo);
    }
}