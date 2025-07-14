package ifsc.strategy;

public class ValidacaoPadrao implements Validacao {

    private final int horasPadrao;

    public ValidacaoPadrao(int horasPadrao) {
        this.horasPadrao = horasPadrao;
    }

    @Override
    public ResultadoValidacao validar(int quantidade) {
        if (quantidade <= 0) {
            return new ResultadoValidacao(0, "Quantidade inválida.");
        }
        
        int horasValidadas = quantidade * this.horasPadrao;
        String observacao = String.format(
            "Aplicado valor padrão de %dh para %d atividade(s) sem carga horária especificada.",
            this.horasPadrao, quantidade
        );

        return new ResultadoValidacao(horasValidadas, observacao);
    }

    @Override
    public String getDescricaoRegra() {
        return String.format("Quantidade de participações (valor padrão de %dh para cada)", this.horasPadrao);
    }
} 
