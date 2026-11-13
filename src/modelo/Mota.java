package modelo;

public class Mota extends Veiculo {

    public Mota(String matricula, String marca, String modelo) {
        super(matricula, marca, modelo);
    }

    @Override
    public TipoVaga getTipo() {
        return TipoVaga.MOTA;
    }
}