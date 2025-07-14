package ifsc.ui;

import java.util.List;

import ifsc.model.AtividadeComplementar;
import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;

public class ConsoleView {

    public void exibirBoasVindas() {
        System.out.println("======================================================");
        System.out.println("  SISTEMA DE AVALIAÇÃO DE ATIVIDADES COMPLEMENTARES");
        System.out.println("======================================================");
    }

    public void pedirMatricula() {
        System.out.print("Informe a matrícula do aluno: ");
    }

    public void exibirMenuPrincipal() {
        System.out.println("\n== MENU DE MODALIDADES ==");
        System.out.println("1) Ensino");
        System.out.println("2) Pesquisa e Inovação");
        System.out.println("3) Extensão");
        System.out.println("4) Complementação");
        System.out.println("0) Finalizar e iniciar avaliação");
        System.out.print("Escolha a modalidade (0-finalizar): ");
    }

    public void exibirSubmenuAtividades(Modalidade modalidade) {
        System.out.printf("\n-- Atividades em %s --\n", modalidade.getNome());
        List<AtividadeComplementar> atividades = modalidade.getAtividadesDisponiveis();

        for (int i = 0; i < atividades.size(); i++) {
            AtividadeComplementar atividade = atividades.get(i);
            String regra = (atividade.getEstrategiaValidacao() != null)
                    ? atividade.getEstrategiaValidacao().getDescricaoRegra()
                    : "ERRO: Regra não definida!";

            System.out.printf("%d) %-40s (Regra: %s)\n", (i + 1), atividade.getDescricao(), regra);
        }
        System.out.println("0) Voltar ao menu de modalidades");
        System.out.print("Escolha a atividade (0-voltar): ");
    }

    public void pedirValorParaAtividade(String descricao) {
        System.out.printf("Informe o valor para '%s': ", descricao);
    }

    public void exibirDetalhesAtividadeRevisao(AtividadeRequerida atividade, int numero) {
        System.out.printf("\n--- Avaliando Atividade %d ---\n", numero);
        System.out.printf("  Descrição:        %s\n", atividade.getAtividadeComplementar().getDescricao());
        System.out.printf("  Valor Declarado:    %d (%s)\n", atividade.getHorasDeclaradas(), atividade.getAtividadeComplementar().getEstrategiaValidacao().getDescricaoRegra());
        System.out.printf("  Validação Automática: %dh\n", atividade.getHorasValidadas());
        System.out.printf("  Observação Automática: %s\n", atividade.getObservacao());
        System.out.println("---------------------------------");
    }

    public void exibirMenuRevisao() {
        System.out.println("Escolha uma ação:");
        System.out.println("  1) Manter valor da validação automática");
        System.out.println("  2) Alterar horas (aprovação parcial)");
        System.out.println("  3) Recusar atividade (zerar horas)");
        System.out.print("Sua opção: ");
    }

    public void pedirNovasHoras() {
        System.out.print("Informe as novas horas validadas: ");
    }

    public void pedirJustificativa(String motivo) {
        System.out.printf("Informe a %s: ", motivo);
    }

    public void exibirParecer(String parecer) {
        System.out.println("\nGerando parecer final...");
        System.out.println("------------------------------------------------------");
        System.out.println(parecer);
        System.out.println("------------------------------------------------------");
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirMensagemErro(String erro) {
        System.out.println("ERRO: " + erro);
    }

    public void exibirFinalizacao() {
        System.out.println("\nSistema finalizado.");
    }
}
