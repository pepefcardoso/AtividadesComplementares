package ifsc.service;

import ifsc.model.Requerimento;
import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parecer {

    // O método gerar() agora é curto e legível.
public String gerar(Requerimento requerimento) {
    StringBuilder parecer = new StringBuilder();

    // 1. Construir o cabeçalho
    construirCabecalho(parecer, requerimento);

    // 2. Organizar os dados
    Map<Modalidade, List<AtividadeRequerida>> atividadesAgrupadas = agruparAtividadesPorModalidade(requerimento);

    // 3. Processar os dados de cada modalidade e calcular o total de horas
    int totalHorasValidadas = processarModalidades(parecer, atividadesAgrupadas, requerimento);

    // 4. Construir o rodapé com o resumo final
    construirResumoGeral(parecer, totalHorasValidadas);

    return parecer.toString();
}

    // Coloque estes métodos privados dentro da mesma classe Parecer

private void construirCabecalho(StringBuilder parecer, Requerimento requerimento) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    parecer.append("=== PARECER DE VALIDAÇÃO ===\n");
    parecer.append("Matrícula: ").append(requerimento.getMatricula()).append("\n");
    parecer.append("Data emissão: ").append(dtf.format(LocalDate.now())).append("\n");
    parecer.append("Total de Horas Exigidas no Curso: ").append(requerimento.getTotalHorasCurso()).append("h\n");
}

private Map<Modalidade, List<AtividadeRequerida>> agruparAtividadesPorModalidade(Requerimento requerimento) {
    Map<Modalidade, List<AtividadeRequerida>> atividadesPorModalidade = new HashMap<>();
    for (AtividadeRequerida atividadeRequerida : requerimento.getAtividadesSubmetidas()) {
        Modalidade modalidade = atividadeRequerida.getAtividadeComplementar().getModalidade();
        atividadesPorModalidade.computeIfAbsent(modalidade, k -> new ArrayList<>()).add(atividadeRequerida);
    }
    return atividadesPorModalidade;
}

private int processarModalidades(StringBuilder parecer, Map<Modalidade, List<AtividadeRequerida>> atividadesAgrupadas, Requerimento requerimento) {
    int totalHorasValidadasFinal = 0;
    int contadorAtividadeGlobal = 1;

    for (Map.Entry<Modalidade, List<AtividadeRequerida>> entry : atividadesAgrupadas.entrySet()) {
        Modalidade modalidade = entry.getKey();
        List<AtividadeRequerida> atividadesDaModalidade = entry.getValue();
        
        int limiteHorasModalidade = (int) (requerimento.getTotalHorasCurso() * modalidade.getPercentualLimite());

        parecer.append(String.format("\n--- MODALIDADE: %s (Limite da modalidade: %dh) ---\n", modalidade.getNome(), limiteHorasModalidade));

        int subtotalValidadoModalidade = 0;
        for (AtividadeRequerida atividadeRequerida : atividadesDaModalidade) {
            formatarAtividadeIndividual(parecer, atividadeRequerida, contadorAtividadeGlobal++);
            subtotalValidadoModalidade += atividadeRequerida.getHorasValidadas();
        }

        int horasFinaisModalidade = Math.min(subtotalValidadoModalidade, limiteHorasModalidade);
        formatarResumoModalidade(parecer, subtotalValidadoModalidade, limiteHorasModalidade, horasFinaisModalidade);

        totalHorasValidadasFinal += horasFinaisModalidade;
    }
    return totalHorasValidadasFinal;
}

private void formatarAtividadeIndividual(StringBuilder parecer, AtividadeRequerida atividadeRequerida, int contador) {
    parecer.append(String.format("Atividade %d:\n", contador));
    parecer.append(String.format("  Descrição:        %s\n", atividadeRequerida.getAtividadeComplementar().getDescricao()));
    String regraDesc = atividadeRequerida.getAtividadeComplementar().getEstrategiaValidacao().getDescricaoRegra();
    parecer.append(String.format("  Declarado:        %d (%s)\n", atividadeRequerida.getHorasDeclaradas(), regraDesc));
    parecer.append(String.format("  Horas validadas:  %dh\n", atividadeRequerida.getHorasValidadas()));
    parecer.append(String.format("  Observação:       %s\n\n", atividadeRequerida.getObservacao()));
}

private void formatarResumoModalidade(StringBuilder parecer, int subtotal, int limite, int valorFinal) {
    parecer.append(String.format("* Subtotal de horas na modalidade: %dh\n", subtotal));
    if (subtotal > limite) {
        parecer.append(String.format("* Observação: O subtotal (%dh) excede o limite da modalidade. Valor ajustado para %dh.\n",
                subtotal, valorFinal));
    }
}

private void construirResumoGeral(StringBuilder parecer, int totalHorasValidadas) {
    parecer.append("\n=== Resumo Geral ===\n");
    parecer.append(String.format("  Total de horas validadas no requerimento: %dh\n", totalHorasValidadas));
}
}