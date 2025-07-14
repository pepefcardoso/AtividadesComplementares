package ifsc.strategy;

public class ValidacaoPorMultiplicador implements Validacao {

    private final int horasPorUnidade;
    private final String nomeDaUnidade; // Ex: "mês", "semestre"

    public ValidacaoPorMultiplicador(int horasPorUnidade, String nomeDaUnidade) {
        this.horasPorUnidade = horasPorUnidade;
        this.nomeDaUnidade = nomeDaUnidade;
    }

    @Override
    public ResultadoValidacao validar(int quantidadeDeUnidades) {
        if (quantidadeDeUnidades <= 0) {
            return new ResultadoValidacao(0, "Quantidade inválida.");
        }

        int horasValidadas = quantidadeDeUnidades * this.horasPorUnidade;
        String observacao = String.format(
            "%d %s x %dh/%s = %dh horas validadas.",
            quantidadeDeUnidades, this.nomeDaUnidade, this.horasPorUnidade, this.nomeDaUnidade, horasValidadas
        );

        return new ResultadoValidacao(horasValidadas, observacao);
    }

    @Override
    public String getDescricaoRegra() {
        return String.format("Número de %ses (cada %s vale %dh)", this.nomeDaUnidade, this.nomeDaUnidade, this.horasPorUnidade);
    }
} 
