package ifsc.ui;

import java.util.HashMap; // Importar HashMap
import java.util.InputMismatchException;     // Importar Map
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ifsc.factory.ModalidadeFactory;
import ifsc.model.AtividadeComplementar;
import ifsc.model.AtividadeRequerida;
import ifsc.model.Modalidade;
import ifsc.model.Requerimento;
import ifsc.model.TipoRevisao;
import ifsc.service.AvaliacaoService;
import ifsc.service.Parecer;
import ifsc.service.RevisaoService;
import ifsc.ui.command.FinalizarCommand;
import ifsc.ui.command.MenuCommand;
import ifsc.ui.command.ProcessarModalidadeCommand;

public class ConsoleUI implements RevisaoUI {

    private final Scanner scanner;
    private Requerimento requerimento;
    private final AvaliacaoService avaliacaoService;
    private final Parecer geradorParecer;
    private final RevisaoService revisaoService;

    private final Map<Integer, MenuCommand> menuCommands;
    private boolean executando = true;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.avaliacaoService = new AvaliacaoService();
        this.geradorParecer = new Parecer();
        this.revisaoService = new RevisaoService(this);

        this.menuCommands = new HashMap<>();
        inicializarComandos();
    }

    private void inicializarComandos() {
        menuCommands.put(1, new ProcessarModalidadeCommand(this, 1));
        menuCommands.put(2, new ProcessarModalidadeCommand(this, 2));
        menuCommands.put(3, new ProcessarModalidadeCommand(this, 3));
        menuCommands.put(4, new ProcessarModalidadeCommand(this, 4));
        menuCommands.put(0, new FinalizarCommand(this));
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
        while (this.executando) {
            exibirMenuPrincipal();
            int escolha = lerOpcao();

            MenuCommand command = menuCommands.get(escolha);

            if (command != null) {
                command.execute();
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public void finalizarAvaliacao() {
        revisaoService.iniciarRevisaoInterativa(this.requerimento);
        finalizarEGerarParecer();
        this.executando = false;
    }

    public void processarModalidade(int tipoModalidade) {
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

    private void exibirMenuPrincipal() {
        System.out.println("\n== MENU DE MODALIDADES ==");
        System.out.println("1) Ensino");
        System.out.println("2) Pesquisa e Inovação");
        System.out.println("3) Extensão");
        System.out.println("4) Complementação");
        System.out.println("0) Finalizar e iniciar avaliação");
        System.out.print("Escolha a modalidade (0-finalizar): ");
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

    @Override
    public void exibirDetalhesAtividade(AtividadeRequerida atividade, int numero) {
        System.out.printf("\n--- Avaliando Atividade %d ---\n", numero);
        System.out.printf("  Descrição:        %s\n", atividade.getAtividadeComplementar().getDescricao());
        System.out.printf("  Valor Declarado:    %d (%s)\n", atividade.getHorasDeclaradas(), atividade.getAtividadeComplementar().getEstrategiaValidacao().getDescricaoRegra());
        System.out.printf("  Validação Automática: %dh\n", atividade.getHorasValidadas());
        System.out.printf("  Observação Automática: %s\n", atividade.getObservacao());
        System.out.println("---------------------------------");
    }

    @Override
    public TipoRevisao perguntarAcaoRevisao() {
        System.out.println("Escolha uma ação:");
        System.out.println("  1) Manter valor da validação automática");
        System.out.println("  2) Alterar horas (aprovação parcial)");
        System.out.println("  3) Recusar atividade (zerar horas)");
        System.out.print("Sua opção: ");
        int escolhaInt = lerOpcao();
        return TipoRevisao.fromValor(escolhaInt);
    }

    @Override
    public int perguntarNovasHoras() {
        System.out.print("Informe as novas horas validadas: ");
        return lerOpcao();
    }

    @Override
    public String perguntarJustificativa(String motivo) {
        System.out.printf("Informe a %s: ", motivo);
        return scanner.nextLine();
    }
}
