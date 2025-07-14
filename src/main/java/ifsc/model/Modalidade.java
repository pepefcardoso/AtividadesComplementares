package ifsc.model;

import java.util.List;

public class Modalidade {
    private String nome;
    private List<AtividadeComplementar> atividadesDisponiveis;
    private double percentualLimite;

    public Modalidade(String nome, double percentualLimite) {
        this.nome = nome;
        this.percentualLimite = percentualLimite;
    }

    public String getNome() {
        return nome;
    }

    public List<AtividadeComplementar> getAtividadesDisponiveis() {
        return atividadesDisponiveis;
    }

    public void setAtividadesDisponiveis(List<AtividadeComplementar> atividadesDisponiveis) {
        this.atividadesDisponiveis = atividadesDisponiveis;
    }

    public double getPercentualLimite() {
        return percentualLimite;
    }
}