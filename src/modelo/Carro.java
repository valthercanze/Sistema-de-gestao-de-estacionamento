package modelo;

public class Carro extends Veiculo {

    public Carro(String matricula, String marca, String modelo) {
        super(matricula, marca, modelo);
    }

    @Override
    public TipoVaga getTipo() {
        return TipoVaga.CARRO;
    }
}