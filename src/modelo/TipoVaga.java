package modelo;

public enum TipoVaga {
    CARRO("Carro"),
    MOTA("Mota"),
    CAMIAO("Camião");

    private final String descricao;

    TipoVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}