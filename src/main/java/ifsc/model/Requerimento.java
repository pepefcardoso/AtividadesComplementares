package ifsc.model;

import java.util.List;
import java.util.ArrayList;

public class Requerimento {
    private String matricula;
    private final int totalHorasCurso = 200;
    private List<AtividadeRequerida> atividadesSubmetidas = new ArrayList<>();

    public Requerimento(String matricula) {
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public List<AtividadeRequerida> getAtividadesSubmetidas() {
        return atividadesSubmetidas;
    }

    public void adicionarAtividade(AtividadeRequerida atividade) {
        atividadesSubmetidas.add(atividade);
    }

    public int getTotalHorasCurso() {
        return totalHorasCurso;
    }
}
