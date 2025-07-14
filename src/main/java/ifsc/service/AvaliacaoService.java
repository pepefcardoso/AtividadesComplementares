package ifsc.service;

import ifsc.model.AtividadeComplementar;
import ifsc.model.AtividadeRequerida;
import ifsc.strategy.Validacao;
import ifsc.strategy.ResultadoValidacao;

public class AvaliacaoService {
    public AtividadeRequerida avaliar(AtividadeComplementar atividade, int valorDeclarado) {
    Validacao estrategia = atividade.getEstrategiaValidacao();
    
    // 1. A Service recebe o "pacote de dados" retornado pela estratégia.
    ResultadoValidacao resultado = estrategia.validar(valorDeclarado);
    
    // 2. A Service "desempacota" os dados usando os getters.
    int horasCalculadas = resultado.getHorasValidadas();
    String obsCalculada = resultado.getObservacao();
    
    // 3. Usa os dados desempacotados para criar o objeto final.
    AtividadeRequerida atividadeRequerida = new AtividadeRequerida(
        atividade,
        valorDeclarado,
        horasCalculadas,
        obsCalculada
    );
    
    // O objeto 'resultado' já cumpriu sua missão. Ele será descartado.
    return atividadeRequerida;
    }
}