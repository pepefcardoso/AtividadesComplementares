package ifsc.service;

import ifsc.model.AtividadeComplementar;
import ifsc.model.AtividadeRequerida;
import ifsc.strategy.ResultadoValidacao;
import ifsc.strategy.Validacao;

public class AvaliacaoService {

    public AtividadeRequerida avaliar(AtividadeComplementar atividade, int valorDeclarado) {
        Validacao estrategia = atividade.getEstrategiaValidacao();

        ResultadoValidacao resultado = estrategia.validar(valorDeclarado);

        int horasCalculadas = resultado.getHorasValidadas();
        String obsCalculada = resultado.getObservacao();

        AtividadeRequerida atividadeRequerida = new AtividadeRequerida(
                atividade,
                valorDeclarado,
                horasCalculadas,
                obsCalculada
        );

        return atividadeRequerida;
    }
}
