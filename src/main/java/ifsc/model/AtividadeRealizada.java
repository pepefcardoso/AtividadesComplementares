package ifsc.model;

public class AtividadeRealizada {
    private int id;
    private Requerimento requerimento;
    private AtividadeComplementar atividadeComplementar;
    private int horasApresentadas;
    private String descricao;

    public AtividadeRealizada(int id, Requerimento requerimento, AtividadeComplementar atividadeComplementar, int horasApresentadas, String descricao) {
        this.id = id;
        this.requerimento = requerimento;
        this.atividadeComplementar = atividadeComplementar;
        this.horasApresentadas = horasApresentadas;
        this.descricao = descricao;
    }
}
