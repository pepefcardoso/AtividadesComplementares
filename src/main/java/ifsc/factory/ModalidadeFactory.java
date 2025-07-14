package ifsc.factory;

import java.util.Arrays;
import java.util.List;

import ifsc.model.AtividadeComplementar;
import ifsc.model.Modalidade;
import ifsc.strategy.ValidacaoHoraDeclaradaComLimite;
import ifsc.strategy.ValidacaoPorMultiplicador;
import ifsc.strategy.ValidacaoPadrao;
import ifsc.strategy.ValidacaoHoraFixa;

public class ModalidadeFactory {
    public static Modalidade criarModalidade(int tipo) {
        switch (tipo) {
            case 1:
                return criarModalidadeEnsino();
            case 2:
                return criarModalidadePesquisa();
            case 3:
                return criarModalidadeExtensao();
            case 4:
                return criarModalidadeComplementacao();
            default:
                return null;
        }
    }

    private static Modalidade criarModalidadeEnsino() {
        Modalidade ensino = new Modalidade("Ensino", 0.40);
        
        List<AtividadeComplementar> atividades = Arrays.asList(
            // Atividade com limite de horas
            new AtividadeComplementar("Visita técnica", new ValidacaoHoraDeclaradaComLimite(8), "Declaração de participação", ensino),
            
            // Atividade com valor padrão
            new AtividadeComplementar("Participação em palestra (sem horas no certificado)", new ValidacaoPadrao(4), "Certificado de participação", ensino)
        );

        ensino.setAtividadesDisponiveis(atividades);
        
        return ensino;
    }
    
    private static Modalidade criarModalidadePesquisa() {
        Modalidade pesquisa = new Modalidade("Pesquisa e Inovação", 0.40);
        
        List<AtividadeComplementar> atividades = Arrays.asList(
            new AtividadeComplementar("Publicação de artigo em periódico", new ValidacaoHoraFixa(15), "Cópia do artigo", pesquisa),
            new AtividadeComplementar("Apresentação de trabalho em evento", new ValidacaoHoraFixa(10), "Certificado de apresentação", pesquisa)
        );

        pesquisa.setAtividadesDisponiveis(atividades);

        return pesquisa;
    }
    
    private static Modalidade criarModalidadeExtensao() {
        Modalidade extensao = new Modalidade("Extensão", 0.40);
        
        List<AtividadeComplementar> atividades = Arrays.asList(
            new AtividadeComplementar("Estágio não obrigatório na área", new ValidacaoPorMultiplicador(25, "mês"), "Contrato de estágio", extensao)
        );
        
        extensao.setAtividadesDisponiveis(atividades);
        
        return extensao;
    }
    
    private static Modalidade criarModalidadeComplementacao() {
        Modalidade complementacao = new Modalidade("Complementação", 0.20);
        
        List<AtividadeComplementar> atividades = Arrays.asList(
            new AtividadeComplementar("Participação em evento cultural", new ValidacaoHoraFixa(5), "Certificado ou ingresso", complementacao),
            new AtividadeComplementar("Curso de língua estrangeira", new ValidacaoHoraDeclaradaComLimite(40), "Certificado de conclusão", complementacao),
            new AtividadeComplementar("Trabalho voluntário", new ValidacaoHoraDeclaradaComLimite(30), "Declaração da instituição", complementacao)        );
        
        complementacao.setAtividadesDisponiveis(atividades);
        
        return complementacao;
    }
}