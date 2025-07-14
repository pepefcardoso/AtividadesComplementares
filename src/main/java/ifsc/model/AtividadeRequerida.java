package ifsc.model;

public class AtividadeRequerida {
    private AtividadeComplementar atividadeComplementar;
    private int horasDeclaradas;
    private int horasValidadas;
    private String observacao;

    public AtividadeRequerida(AtividadeComplementar atividadeComplementar, int horasDeclaradas, int horasValidadas, String observacao) {
        this.atividadeComplementar = atividadeComplementar;
        this.horasDeclaradas = horasDeclaradas;
        this.horasValidadas = horasValidadas;
        this.observacao = observacao;
    }

    public AtividadeComplementar getAtividadeComplementar() {
        return atividadeComplementar;
    }

    public int getHorasDeclaradas() {
        return horasDeclaradas;
    }

    public int getHorasValidadas() {
        return horasValidadas;
    }

    public String getObservacao() {
        return observacao;
    }
    
    public void setHorasValidadas(int horasValidadas) {
        this.horasValidadas = horasValidadas;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}