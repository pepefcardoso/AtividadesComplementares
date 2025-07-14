package ifsc.service;

import ifsc.model.AtividadeRequerida;

public class RevisaoService {

    public void revisar(AtividadeRequerida atividade, int novasHoras, String novaObservacao, int tipoRevisao) {
        switch (tipoRevisao) {
            case 2: // Aprovar parcialmente
                atividade.setHorasValidadas(novasHoras);
                atividade.setObservacao("Ajuste manual do avaliador: " + novaObservacao);
                System.out.println("-> Horas alteradas com sucesso.");
                break;
            case 3: // Recusar
                atividade.setHorasValidadas(0);
                atividade.setObservacao("Recusado pelo avaliador: " + novaObservacao);
                System.out.println("-> Atividade recusada com sucesso.");
                break;
            default: // Aprovar ou caso inválido
                 System.out.println("-> Valor original mantido.");
                break;
        }
    }
}