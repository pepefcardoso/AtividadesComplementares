package ifsc.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ifsc.model.AtividadeRequerida;
import ifsc.model.Requerimento;
import ifsc.model.TipoRevisao;

public class RevisaoService {

    public void iniciarRevisaoInterativa(Requerimento requerimento, Scanner scanner) {
        System.out.println("\n======================================================");
        System.out.println("            INICIANDO MODO DE AVALIAÇÃO");
        System.out.println("======================================================");

        List<AtividadeRequerida> atividades = requerimento.getAtividadesSubmetidas();

        if (atividades.isEmpty()) {
            System.out.println("Nenhuma atividade foi submetida. Nenhum parecer a ser gerado.");
            return;
        }

        for (int i = 0; i < atividades.size(); i++) {
            AtividadeRequerida atividadeAtual = atividades.get(i);
            avaliarAtividadeIndividualmente(atividadeAtual, i + 1, scanner);
        }

        System.out.println("\nRevisão de todas as atividades concluída.");
    }

    private void avaliarAtividadeIndividualmente(AtividadeRequerida atividade, int numeroAtividade, Scanner scanner) {
        System.out.printf("\n--- Avaliando Atividade %d ---\n", numeroAtividade);
        System.out.printf("  Descrição:        %s\n", atividade.getAtividadeComplementar().getDescricao());
        System.out.printf("  Valor Declarado:    %d (%s)\n", atividade.getHorasDeclaradas(), atividade.getAtividadeComplementar().getEstrategiaValidacao().getDescricaoRegra());
        System.out.printf("  Validação Automática: %dh\n", atividade.getHorasValidadas());
        System.out.printf("  Observação Automática: %s\n", atividade.getObservacao());
        System.out.println("---------------------------------");

        System.out.println("Escolha uma ação:");
        System.out.println("  1) Manter valor da validação automática");
        System.out.println("  2) Alterar horas (aprovação parcial)");
        System.out.println("  3) Recusar atividade (zerar horas)");
        System.out.print("Sua opção: ");

        int escolhaInt = lerOpcao(scanner);
        TipoRevisao escolha = TipoRevisao.fromValor(escolhaInt);

        int novasHoras = 0;
        String novaObservacao = "";

        if (escolha == TipoRevisao.ALTERAR_HORAS) {
            System.out.print("Informe as novas horas validadas: ");
            novasHoras = lerOpcao(scanner);
            System.out.print("Informe a justificativa da alteração: ");
            novaObservacao = scanner.nextLine();
        } else if (escolha == TipoRevisao.RECUSAR_ATIVIDADE) {
            System.out.print("Informe o motivo da recusa: ");
            novaObservacao = scanner.nextLine();
        }

        this.revisar(atividade, novasHoras, novaObservacao, escolha);
    }

    private void revisar(AtividadeRequerida atividade, int novasHoras, String novaObservacao, TipoRevisao tipo) {
        switch (tipo) {
            case ALTERAR_HORAS:
                atividade.setHorasValidadas(novasHoras);
                atividade.setObservacao("Ajuste manual do avaliador: " + novaObservacao);
                System.out.println("-> Horas alteradas com sucesso.");
                break;
            case RECUSAR_ATIVIDADE:
                atividade.setHorasValidadas(0);
                atividade.setObservacao("Recusado pelo avaliador: " + novaObservacao);
                System.out.println("-> Atividade recusada com sucesso.");
                break;
            case MANTER_VALIDACAO:
            default:
                System.out.println("-> Valor original mantido.");
                break;
        }
    }

    private int lerOpcao(Scanner scanner) {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            return opcao;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
            return -1;
        }
    }
}
