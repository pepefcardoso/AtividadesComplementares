package ifsc.model;

import ifsc.strategy.Validacao;

public class AtividadeComplementar{
    private String descricao;
    private Validacao estrategiaValidacao;
    private String documentacaoExigida;
    private Modalidade modalidade;


    public AtividadeComplementar(String descricao, Validacao estrategiaValidacao, String documentacaoExigida, Modalidade modalidade) {
        this.descricao = descricao;
        this.estrategiaValidacao = estrategiaValidacao;
        this.documentacaoExigida = documentacaoExigida;
        this.modalidade = modalidade;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public String getDescricao() {
        return descricao;
    }


    public Validacao getEstrategiaValidacao() {
        return estrategiaValidacao;
    }


    public String getDocumentacaoExigida() {
        return documentacaoExigida;
    }

    

}