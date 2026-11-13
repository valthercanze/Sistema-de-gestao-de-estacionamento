package controle;

import excecoes.MatriculaInvalidaExcecao;
import excecoes.PersistenciaExcecao;
import excecoes.VeiculoDuplicadoExcecao;
import excecoes.DadosInvalidosExcecao;
import excecoes.VagasEsgotadasExcecao;
import modelo.Bilhete;
import modelo.ValidadorMatricula;
import modelo.Vaga;
import modelo.Veiculo;
import dao.BilheteDAO;

public class EntradaControle {

    private final BilheteDAO bilheteDAO;
    private final VagaControle vagaControle;

    public EntradaControle(BilheteDAO bilheteDAO, VagaControle vagaControle) {
        this.bilheteDAO = bilheteDAO;
        this.vagaControle = vagaControle;
    }

    public Bilhete registarEntrada(Veiculo v)
            throws DadosInvalidosExcecao, MatriculaInvalidaExcecao,
                   VeiculoDuplicadoExcecao, VagasEsgotadasExcecao,
                   PersistenciaExcecao {

        validarCampos(v);

        String matricula = ValidadorMatricula.validar(v.getMatricula());

        if (bilheteDAO.veiculoEstaDentro(matricula)) {
            throw new VeiculoDuplicadoExcecao(
                "Matrícula duplicada: o veículo " + matricula + " já está no parque");
        }

        Vaga vaga = vagaControle.atribuirVaga(v);

        Bilhete bilhete = new Bilhete(v, vaga);
        try {
            bilheteDAO.inserir(bilhete);
        } catch (PersistenciaExcecao e) {
            vagaControle.libertarVaga(vaga.getNumero());
            throw e;
        }
        return bilhete;
    }

    private void validarCampos(Veiculo v) throws DadosInvalidosExcecao {
        if (v == null) {
            throw new DadosInvalidosExcecao("O veículo não pode ser nulo");
        }
        if (v.getMatricula() == null || v.getMatricula().isBlank()) {
            throw new DadosInvalidosExcecao("A matrícula deve estar preenchida");
        }
        if (v.getMarca() == null || v.getMarca().isBlank()) {
            throw new DadosInvalidosExcecao("A marca deve estar preenchida");
        }
        if (v.getModelo() == null || v.getModelo().isBlank()) {
            throw new DadosInvalidosExcecao("O modelo deve estar preenchido");
        }
        if (v.getTipo() == null) {
            throw new DadosInvalidosExcecao("O tipo de veículo deve estar definido");
        }
    }
}