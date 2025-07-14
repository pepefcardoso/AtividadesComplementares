package ifsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;
import ifsc.model.Requerimento;

public class Parecer {

    public String gerar(Requerimento requerimento) {
        ParecerBuilder builder = new ParecerBuilder();

        builder.comCabecalho(requerimento);

        Map<Modalidade, List<AtividadeRequerida>> atividadesAgrupadas = agruparAtividadesPorModalidade(requerimento);

        int totalHorasValidadas = processarModalidades(builder, atividadesAgrupadas, requerimento);

        builder.comResumoGeral(totalHorasValidadas);

        return builder.build();
    }

    private Map<Modalidade, List<AtividadeRequerida>> agruparAtividadesPorModalidade(Requerimento requerimento) {
        Map<Modalidade, List<AtividadeRequerida>> atividadesPorModalidade = new HashMap<>();
        for (AtividadeRequerida atividadeRequerida : requerimento.getAtividadesSubmetidas()) {
            Modalidade modalidade = atividadeRequerida.getAtividadeComplementar().getModalidade();
            atividadesPorModalidade.computeIfAbsent(modalidade, k -> new ArrayList<>()).add(atividadeRequerida);
        }
        return atividadesPorModalidade;
    }

    private int processarModalidades(ParecerBuilder builder, Map<Modalidade, List<AtividadeRequerida>> atividadesAgrupadas, Requerimento requerimento) {
        int totalHorasValidadasFinal = 0;
        int contadorAtividadeGlobal = 1;

        for (Map.Entry<Modalidade, List<AtividadeRequerida>> entry : atividadesAgrupadas.entrySet()) {
            Modalidade modalidade = entry.getKey();
            List<AtividadeRequerida> atividadesDaModalidade = entry.getValue();

            int limiteHorasModalidade = (int) (requerimento.getTotalHorasCurso() * modalidade.getPercentualLimite());

            builder.comInicioModalidade(modalidade, limiteHorasModalidade);

            int subtotalValidadoModalidade = 0;
            for (AtividadeRequerida atividadeRequerida : atividadesDaModalidade) {
                builder.comDetalhesDeAtividade(atividadeRequerida, contadorAtividadeGlobal++);
                subtotalValidadoModalidade += atividadeRequerida.getHorasValidadas();
            }

            int horasFinaisModalidade = Math.min(subtotalValidadoModalidade, limiteHorasModalidade);

            builder.comResumoModalidade(subtotalValidadoModalidade, limiteHorasModalidade, horasFinaisModalidade);

            totalHorasValidadasFinal += horasFinaisModalidade;
        }
        return totalHorasValidadasFinal;
    }
}
