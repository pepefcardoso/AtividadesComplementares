package ifsc.ui;

import ifsc.model.AtividadeRequerida;
import ifsc.model.TipoRevisao;

public interface RevisaoUI {

    void exibirDetalhesAtividade(AtividadeRequerida atividade, int numero);

    TipoRevisao perguntarAcaoRevisao();

    int perguntarNovasHoras();

    String perguntarJustificativa(String motivo);
}
