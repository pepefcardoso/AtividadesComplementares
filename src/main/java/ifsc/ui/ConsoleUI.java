package ifsc.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ifsc.factory.ModalidadeFactory;
import ifsc.model.AtividadeComplementar;
import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;
import ifsc.model.Requerimento;
import ifsc.service.AvaliacaoService;
import ifsc.service.Parecer;
import ifsc.service.RevisaoService;

public class ConsoleUI {

    private final Scanner scanner;
    private Requerimento requerimento;
    private final AvaliacaoService avaliacaoService;
    private final Parecer geradorParecer;
    private final RevisaoService revisaoService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.avaliacaoService = new AvaliacaoService();
        this.geradorParecer = new Parecer();
        this.revisaoService = new RevisaoService();
    }

    public void iniciar() {
        System.out.println("======================================================");
        System.out.println("  SISTEMA DE AVALIAÇÃO DE ATIVIDADES COMPLEMENTARES");
        System.out.println("======================================================");

        String matricula;

        do {
            System.out.print("Informe a matrícula do aluno: ");
            matricula = scanner.nextLine();

            if (matricula.trim().isEmpty()) {
                System.out.println("ERRO: A matrícula não pode estar em branco. Por favor, tente novamente.");
            }

        } while (matricula.trim().isEmpty());

        this.requerimento = new Requerimento(matricula);

        loopPrincipal();
        fecharRecursos();
    }

    private void loopPrincipal() {
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int escolha = lerOpcao();

            switch (escolha) {
                case 1:
                case 2:
                case 3:
                case 4:
                    processarModalidade(escolha);
                    break;
                case 0:
                    revisaoService.iniciarRevisaoInterativa(this.requerimento, this.scanner);
                    finalizarEGerarParecer();
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n== MENU DE MODALIDADES ==");
        System.out.println("1) Ensino");
        System.out.println("2) Pesquisa e Inovação");
        System.out.println("3) Extensão");
        System.out.println("4) Complementação");
        System.out.println("0) Finalizar e iniciar avaliação");
        System.out.print("Escolha a modalidade (0-finalizar): ");
    }

    private void processarModalidade(int tipoModalidade) {
        Modalidade modalidade = ModalidadeFactory.criarModalidade(tipoModalidade);
        if (modalidade == null) {
            System.out.println("Modalidade não implementada.");
            return;
        }

        while (true) {
            exibirSubmenuAtividades(modalidade);
            int escolhaAtividade = lerOpcao();

            if (escolhaAtividade == 0) {
                break;
            }

            List<AtividadeComplementar> atividades = modalidade.getAtividadesDisponiveis();
            if (escolhaAtividade > 0 && escolhaAtividade <= atividades.size()) {
                AtividadeComplementar atividadeEscolhida = atividades.get(escolhaAtividade - 1);
                processarAtividade(atividadeEscolhida);
            } else {
                System.out.println("Opção de atividade inválida.");
            }
        }
    }

    private void exibirSubmenuAtividades(Modalidade modalidade) {
        System.out.printf("\n-- Atividades em %s --\n", modalidade.getNome());
        List<AtividadeComplementar> atividades = modalidade.getAtividadesDisponiveis();

        for (int i = 0; i < atividades.size(); i++) {
            AtividadeComplementar atividade = atividades.get(i);
            String regra;

            if (atividade.getEstrategiaValidacao() != null) {
                regra = atividade.getEstrategiaValidacao().getDescricaoRegra();
            } else {
                regra = "ERRO: Regra não definida!";
                System.out.printf("ALERTA DE DESENVOLVEDOR: A atividade '%s' está sem estratégia de validação!\n", atividade.getDescricao());
            }

            System.out.printf("%d) %-40s (Regra: %s)\n", (i + 1), atividade.getDescricao(), regra);
        }
        System.out.println("0) Voltar ao menu de modalidades");
        System.out.print("Escolha a atividade (0-voltar): ");
    }

    private void processarAtividade(AtividadeComplementar atividade) {
        System.out.printf("Informe o valor para '%s': ", atividade.getDescricao());
        int valorDeclarado = lerOpcao();

        if (valorDeclarado > 0) {
            AtividadeRequerida atividadeProcessada = avaliacaoService.avaliar(atividade, valorDeclarado);
            requerimento.adicionarAtividade(atividadeProcessada);
            System.out.println("-> Atividade adicionada ao requerimento.\n");
        } else {
            System.out.println("Valor inválido. A atividade não foi adicionada.");
        }
    }

    private void finalizarEGerarParecer() {
        if (requerimento.getAtividadesSubmetidas().isEmpty()) {
            return;
        }
        System.out.println("\nGerando parecer final...");
        String parecer = geradorParecer.gerar(this.requerimento);
        System.out.println("------------------------------------------------------");
        System.out.println(parecer);
        System.out.println("------------------------------------------------------");
    }

    private int lerOpcao() {
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

    private void fecharRecursos() {
        System.out.println("\nSistema finalizado.");
        scanner.close();
    }
}
