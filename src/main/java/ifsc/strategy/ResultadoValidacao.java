package ifsc.strategy;

public class ResultadoValidacao {
    private int horasValidadas;
    private String observacao;

    public ResultadoValidacao(int horasValidadas, String observacao) {
        this.horasValidadas = horasValidadas;
        this.observacao = observacao;
    }

    public int getHorasValidadas() {
        return horasValidadas;
    }

    public String getObservacao() {
        return observacao;
    }
}
