package ifsc.strategy;

public class ValidacaoHoraFixa implements Validacao {

    private final int horasFixasPorUnidade;

    public ValidacaoHoraFixa(int horasFixasPorUnidade) {
        this.horasFixasPorUnidade = horasFixasPorUnidade;
    }

    @Override
    public ResultadoValidacao validar(int quantidade) {
        if (quantidade <= 0) {
            return new ResultadoValidacao(0, "Quantidade inválida.");
        }
        
        int horasValidadas = quantidade * this.horasFixasPorUnidade;
        String observacao = String.format(
            "%d unidade(s) x %dh/unidade = %dh horas validadas.",
            quantidade, this.horasFixasPorUnidade, horasValidadas
        );

        return new ResultadoValidacao(horasValidadas, observacao);
    }

    @Override
    public String getDescricaoRegra() {
        return String.format("Quantidade de participações (cada uma vale %dh)", this.horasFixasPorUnidade);
    }
}
