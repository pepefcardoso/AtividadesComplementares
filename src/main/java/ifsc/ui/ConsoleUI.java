package ifsc.ui;

import java.util.HashMap;
import java.util.InputMismatchException;
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
    private final ConsoleView view;
    private Requerimento requerimento;
    private final AvaliacaoService avaliacaoService;
    private final Parecer geradorParecer;
    private final RevisaoService revisaoService;

    private final Map<Integer, MenuCommand> menuCommands;
    private boolean executando = true;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
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
        view.exibirBoasVindas();
        String matricula;
        do {
            view.pedirMatricula();
            matricula = scanner.nextLine();
            if (matricula.trim().isEmpty()) {
                view.exibirMensagemErro("A matrícula não pode estar em branco. Por favor, tente novamente.");
            }
        } while (matricula.trim().isEmpty());
        this.requerimento = new Requerimento(matricula);
        loopPrincipal();
        fecharRecursos();
    }

    private void loopPrincipal() {
        while (this.executando) {
            view.exibirMenuPrincipal();
            int escolha = lerOpcao();
            MenuCommand command = menuCommands.get(escolha);
            if (command != null) {
                command.execute();
            } else {
                view.exibirMensagem("Opção inválida. Tente novamente.");
            }
        }
    }

    public void finalizarAvaliacao() {
        revisaoService.iniciarRevisaoInterativa(this.requerimento);
        if (!requerimento.getAtividadesSubmetidas().isEmpty()) {
            String parecer = geradorParecer.gerar(this.requerimento);
            view.exibirParecer(parecer);
        }
        this.executando = false;
    }

    public void processarModalidade(int tipoModalidade) {
        Modalidade modalidade = ModalidadeFactory.criarModalidade(tipoModalidade);
        if (modalidade == null) {
            view.exibirMensagem("Modalidade não implementada.");
            return;
        }
        while (true) {
            view.exibirSubmenuAtividades(modalidade);
            int escolhaAtividade = lerOpcao();
            if (escolhaAtividade == 0) {
                break;
            }

            List<AtividadeComplementar> atividades = modalidade.getAtividadesDisponiveis();
            if (escolhaAtividade > 0 && escolhaAtividade <= atividades.size()) {
                processarAtividade(atividades.get(escolhaAtividade - 1));
            } else {
                view.exibirMensagem("Opção de atividade inválida.");
            }
        }
    }

    private void processarAtividade(AtividadeComplementar atividade) {
        view.pedirValorParaAtividade(atividade.getDescricao());
        int valorDeclarado = lerOpcao();
        if (valorDeclarado > 0) {
            AtividadeRequerida atividadeProcessada = avaliacaoService.avaliar(atividade, valorDeclarado);
            requerimento.adicionarAtividade(atividadeProcessada);
            view.exibirMensagem("-> Atividade adicionada ao requerimento.\n");
        } else {
            view.exibirMensagem("Valor inválido. A atividade não foi adicionada.");
        }
    }

    private int lerOpcao() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            return opcao;
        } catch (InputMismatchException e) {
            view.exibirMensagem("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
            return -1;
        }
    }

    private void fecharRecursos() {
        view.exibirFinalizacao();
        scanner.close();
    }

    @Override
    public void exibirDetalhesAtividade(AtividadeRequerida atividade, int numero) {
        view.exibirDetalhesAtividadeRevisao(atividade, numero);
    }

    @Override
    public TipoRevisao perguntarAcaoRevisao() {
        view.exibirMenuRevisao();
        return TipoRevisao.fromValor(lerOpcao());
    }

    @Override
    public int perguntarNovasHoras() {
        view.pedirNovasHoras();
        return lerOpcao();
    }

    @Override
    public String perguntarJustificativa(String motivo) {
        view.pedirJustificativa(motivo);
        return scanner.nextLine();
    }
}
