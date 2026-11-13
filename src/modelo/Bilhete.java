package modelo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bilhete {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private final Veiculo veiculo;
    private final Vaga vaga;
    private final LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private Double valorPago;

    public Bilhete(Veiculo veiculo, Vaga vaga) {
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.horaEntrada = LocalDateTime.now();
    }

    public Bilhete(int id, Veiculo veiculo, Vaga vaga,
                  LocalDateTime horaEntrada, LocalDateTime horaSaida, Double valorPago) {
        this.id = id;
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.valorPago = valorPago;
    }

    public int getId() { return id; }
    public Veiculo getVeiculo() { return veiculo; }
    public Vaga getVaga() { return vaga; }
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public LocalDateTime getHoraSaida() { return horaSaida; }
    public Double getValorPago() { return valorPago; }

    public boolean isAtivo() {
        return horaSaida == null;
    }

    public void fecharBilhete(LocalDateTime saida, double valor) {
        if (!isAtivo()) {
            throw new IllegalStateException("O bilhete já foi fechado");
        }
        this.horaSaida = saida;
        this.valorPago = valor;
    }

    public long calcularMinutos() {
        LocalDateTime fim = (horaSaida != null) ? horaSaida : LocalDateTime.now();
        return Duration.between(horaEntrada, fim).toMinutes();
    }

    @Override
    public String toString() {
        String saida = (horaSaida != null) ? horaSaida.format(FMT) : "ainda no parque";
        String valor = (valorPago != null) ? String.format("%.2f MT", valorPago) : "-";
        return String.format("#%d | %s | Vaga %d | Entrada: %s | Saída: %s | Valor: %s",
                id, veiculo, vaga.getNumero(), horaEntrada.format(FMT), saida, valor);
    }
}