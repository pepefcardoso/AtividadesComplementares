package ifsc.strategy;

public class ValidacaoHoraDeclaradaComLimite implements Validacao {

    private final int limiteHoras;

    public ValidacaoHoraDeclaradaComLimite(int limiteHoras) {
        this.limiteHoras = limiteHoras;
    }

    @Override
    public ResultadoValidacao validar(int horasDeclaradas) {
        int horasValidadas = Math.min(horasDeclaradas, this.limiteHoras);
        String observacao = "-- (sem ajuste)";

        if (horasDeclaradas > this.limiteHoras) {
            observacao = String.format(
                "Horas declaradas (%dh) excedem o limite da atividade (%dh); valor ajustado.",
                horasDeclaradas, this.limiteHoras
            );
        }

        return new ResultadoValidacao(horasValidadas, observacao);
    }

    @Override
    public String getDescricaoRegra() {
        return String.format("Horas a declarar (limite de %dh por atividade)", this.limiteHoras);
    }
}
