package modelo;

import java.util.Objects;

public abstract class Veiculo {

    private final String matricula;
    private final String marca;
    private final String modelo;

    public Veiculo(String matricula, String marca, String modelo) {
        if (!ValidadorMatricula.isValida(matricula)) {
            throw new IllegalArgumentException(
                "Matrícula inválida. Formato esperado: " + ValidadorMatricula.EXEMPLO);
        }
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("A marca deve estar preenchida");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("O modelo deve estar preenchido");
        }
        this.matricula = ValidadorMatricula.normalizar(matricula);
        this.marca = marca.trim();
        this.modelo = modelo.trim();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public abstract TipoVaga getTipo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;
        return matricula.equals(((Veiculo) o).matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

    @Override
    public String toString() {
        return getTipo().getDescricao() + " " + matricula + " (" + marca + " " + modelo + ")";
    }
}