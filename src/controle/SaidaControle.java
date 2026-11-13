package controle;

import excecoes.MatriculaInvalidaExcecao;
import excecoes.PersistenciaExcecao;
import excecoes.VeiculoNaoEncontradoExcecao;
import modelo.Bilhete;
import modelo.ValidadorMatricula;
import dao.BilheteDAO;
import tarifa.Tarifa;
import tarifa.TarifaFabrica;
import java.time.Duration;
import java.time.LocalDateTime;

public class SaidaControle {

    private final BilheteDAO bilheteDAO;
    private final VagaControle vagaControle;

    public SaidaControle(BilheteDAO bilheteDAO, VagaControle vagaControle) {
        this.bilheteDAO = bilheteDAO;
        this.vagaControle = vagaControle;
    }

    public double registarSaida(String matricula)
            throws MatriculaInvalidaExcecao, VeiculoNaoEncontradoExcecao,
                   PersistenciaExcecao {

        Bilhete bilhete = obterBilheteAtivo(matricula);

        LocalDateTime saida = LocalDateTime.now();
        double valor = calcular(bilhete, saida);

        bilheteDAO.registarSaida(bilhete.getVeiculo().getMatricula(), saida, valor);
        bilhete.fecharBilhete(saida, valor);
        vagaControle.libertarVaga(bilhete.getVaga().getNumero());

        return valor;
    }

    public double calcularValor(String matricula)
            throws MatriculaInvalidaExcecao, VeiculoNaoEncontradoExcecao,
                   PersistenciaExcecao {

        Bilhete bilhete = obterBilheteAtivo(matricula);
        return calcular(bilhete, LocalDateTime.now());
    }

    private Bilhete obterBilheteAtivo(String matricula)
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

    private double calcular(Bilhete bilhete, LocalDateTime saida) {
        long minutos = Duration.between(bilhete.getHoraEntrada(), saida).toMinutes();
        Tarifa tarifa = TarifaFabrica.obter(bilhete.getVeiculo().getTipo());
        return tarifa.calcularValor(minutos);
    }
}