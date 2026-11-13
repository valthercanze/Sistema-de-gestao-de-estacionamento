package modelo;

public class Camiao extends Veiculo {

    public Camiao(String matricula, String marca, String modelo) {
        super(matricula, marca, modelo);
    }

    @Override
    public TipoVaga getTipo() {
        return TipoVaga.CAMIAO;
    }
}