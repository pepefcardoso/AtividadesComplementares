package ifsc.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;
import ifsc.model.Requerimento;

public class ParecerBuilder {

    private final StringBuilder parecer;

    public ParecerBuilder() {
        this.parecer = new StringBuilder();
    }

    public ParecerBuilder comCabecalho(Requerimento requerimento) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        parecer.append("=== PARECER DE VALIDAÇÃO ===\n");
        parecer.append("Matrícula: ").append(requerimento.getMatricula()).append("\n");
        parecer.append("Data emissão: ").append(dtf.format(LocalDate.now())).append("\n");
        parecer.append("Total de Horas Exigidas no Curso: ").append(requerimento.getTotalHorasCurso()).append("h\n");
        return this;
    }

    public ParecerBuilder comInicioModalidade(Modalidade modalidade, int limiteHorasModalidade) {
        parecer.append(String.format("\n--- MODALIDADE: %s (Limite da modalidade: %dh) ---\n", modalidade.getNome(), limiteHorasModalidade));
        return this;
    }

    public ParecerBuilder comDetalhesDeAtividade(AtividadeRequerida atividadeRequerida, int contador) {
        parecer.append(String.format("Atividade %d:\n", contador));
        parecer.append(String.format("  Descrição:        %s\n", atividadeRequerida.getAtividadeComplementar().getDescricao()));
        String regraDesc = atividadeRequerida.getAtividadeComplementar().getEstrategiaValidacao().getDescricaoRegra();
        parecer.append(String.format("  Declarado:        %d (%s)\n", atividadeRequerida.getHorasDeclaradas(), regraDesc));
        parecer.append(String.format("  Horas validadas:  %dh\n", atividadeRequerida.getHorasValidadas()));
        parecer.append(String.format("  Observação:       %s\n\n", atividadeRequerida.getObservacao()));
        return this;
    }

    public ParecerBuilder comResumoModalidade(int subtotal, int limite, int valorFinal) {
        parecer.append(String.format("* Subtotal de horas na modalidade: %dh\n", subtotal));
        if (subtotal > limite) {
            parecer.append(String.format("* Observação: O subtotal (%dh) excede o limite da modalidade. Valor ajustado para %dh.\n",
                    subtotal, valorFinal));
        }
        return this;
    }

    public ParecerBuilder comResumoGeral(int totalHorasValidadas) {
        parecer.append("\n=== Resumo Geral ===\n");
        parecer.append(String.format("  Total de horas validadas no requerimento: %dh\n", totalHorasValidadas));
        return this;
    }

    public String build() {
        return parecer.toString();
    }
}
