package controle;

import excecoes.MatriculaInvalidaExcecao;
import excecoes.PersistenciaExcecao;
import excecoes.DadosInvalidosExcecao;
import excecoes.VeiculoNaoEncontradoExcecao;
import modelo.Bilhete;
import modelo.ValidadorMatricula;
import dao.BilheteDAO;

import java.util.List;

public class VeiculoControle {

    private final BilheteDAO bilheteDAO;

    public VeiculoControle(BilheteDAO bilheteDAO) {
        this.bilheteDAO = bilheteDAO;
    }

    public boolean estaVazio() throws PersistenciaExcecao {
        return bilheteDAO.listarAtivos().isEmpty();
    }

    public List<Bilhete> localizar(String texto)
            throws DadosInvalidosExcecao, PersistenciaExcecao {
        if (texto == null || texto.isBlank()) {
            throw new DadosInvalidosExcecao("Escreva o texto a pesquisar");
        }
        return bilheteDAO.pesquisarAtivos(texto.trim());
    }

    public Bilhete obterNoParque(String matricula)
            throws MatriculaInvalidaExcecao, VeiculoNaoEncontradoExcecao,
                   PersistenciaExcecao {

        String normalizada = ValidadorMatricula.validar(matricula);
        Bilhete bilhete = bilheteDAO.procurarAtivoPorMatricula(normalizada);

        if (bilhete == null) {
            throw new VeiculoNaoEncontradoExcecao(
                "O veículo " + normalizada + " não está no parque");
        }
        return bilhete;
    }

    public void atualizarDados(int id, String matriculaAntiga, String novaMatricula, String marca, String modelo)
            throws MatriculaInvalidaExcecao, DadosInvalidosExcecao, PersistenciaExcecao {

        String antigaNormalizada = ValidadorMatricula.validar(matriculaAntiga);
        String novaNormalizada = ValidadorMatricula.validar(novaMatricula);

        if (marca == null || marca.isBlank()) {
            throw new DadosInvalidosExcecao("A marca deve estar preenchida");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new DadosInvalidosExcecao("O modelo deve estar preenchido");
        }

        if (!antigaNormalizada.equals(novaNormalizada) && bilheteDAO.veiculoEstaDentro(novaNormalizada)) {
            throw new DadosInvalidosExcecao("A nova matrícula já pertence a outro veículo que está no parque.");
        }

        int alteradas = bilheteDAO.atualizarDadosVeiculo(
                id, novaNormalizada, marca.trim(), modelo.trim());

        if (alteradas == 0) {
            throw new PersistenciaExcecao("Erro: Registo não encontrado no banco de dados.");
        }
    }
}