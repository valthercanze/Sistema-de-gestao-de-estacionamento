package tarifa;

import modelo.TipoVaga;

public class TarifaFabrica {

    private TarifaFabrica() {}

    public static Tarifa obter(TipoVaga tipo) {
        return switch (tipo) {
            case CARRO  -> new TarifaCarro();
            case MOTA   -> new TarifaMota();
            case CAMIAO -> new TarifaCamiao();
        };
    }
}