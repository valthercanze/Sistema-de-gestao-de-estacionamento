package modelo;

public class Vaga {

    private final int numero;
    private final TipoVaga tipo;
    private boolean ocupada;

    public Vaga(int numero, TipoVaga tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupada = false;
    }

    public int getNumero() {
        return numero;
    }

    public TipoVaga getTipo() {
        return tipo;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        if (ocupada) {
            throw new IllegalStateException("A vaga " + numero + " já está ocupada");
        }
        ocupada = true;
    }

    public void libertar() {
        ocupada = false;
    }

    @Override
    public String toString() {
        return "Vaga " + numero + " [" + tipo.getDescricao() + "] - "
                + (ocupada ? "OCUPADA" : "livre");
    }
}