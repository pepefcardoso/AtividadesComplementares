package ifsc.service;

import java.util.List;

import ifsc.model.AtividadeRequerida;
import ifsc.model.Requerimento;
import ifsc.model.TipoRevisao;
import ifsc.ui.RevisaoUI;

public class RevisaoService {

    private final RevisaoUI revisaoUI;

    public RevisaoService(RevisaoUI revisaoUI) {
        this.revisaoUI = revisaoUI;
    }

    public void iniciarRevisaoInterativa(Requerimento requerimento) {
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
            avaliarAtividadeIndividualmente(atividadeAtual, i + 1);
        }

        System.out.println("\nRevisão de todas as atividades concluída.");
    }

    private void avaliarAtividadeIndividualmente(AtividadeRequerida atividade, int numeroAtividade) {
        revisaoUI.exibirDetalhesAtividade(atividade, numeroAtividade);

        TipoRevisao escolha = revisaoUI.perguntarAcaoRevisao();

        int novasHoras = 0;
        String novaObservacao = "";

        if (escolha == TipoRevisao.ALTERAR_HORAS) {
            novasHoras = revisaoUI.perguntarNovasHoras();
            novaObservacao = revisaoUI.perguntarJustificativa("justificativa da alteração");
        } else if (escolha == TipoRevisao.RECUSAR_ATIVIDADE) {
            novaObservacao = revisaoUI.perguntarJustificativa("motivo da recusa");
        }

        this.revisar(atividade, novasHoras, novaObservacao, escolha);
    }

    private void revisar(AtividadeRequerida atividade, int novasHoras, String novaObservacao, TipoRevisao tipo) {
        if (null == tipo) {
            System.out.println("-> Valor original mantido.");
        } else switch (tipo) {
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
            default:
                System.out.println("-> Valor original mantido.");
                break;
        }
    }
}