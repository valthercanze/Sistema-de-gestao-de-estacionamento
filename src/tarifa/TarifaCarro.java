package tarifa;

public class TarifaCarro implements Tarifa {

    private static final double PRECO_HORA = 100.00; 

    @Override
    public double calcularValor(long minutos) {
        
        return (minutos / 60.0) * PRECO_HORA;
    }
}