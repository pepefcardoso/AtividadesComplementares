package ifsc.factory;

import java.util.Arrays;
import java.util.List;

import ifsc.model.AtividadeComplementar;
import ifsc.model.Modalidade;
import ifsc.strategy.Validacao;
import ifsc.strategy.ValidacaoHoraDeclaradaComLimite;
import ifsc.strategy.ValidacaoHoraFixa;
import ifsc.strategy.ValidacaoPadrao;
import ifsc.strategy.ValidacaoPorMultiplicador;

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
                criarAtividade("Visita técnica", new ValidacaoHoraDeclaradaComLimite(8), "Declaração de participação", ensino),
                criarAtividade("Participação em palestra (sem horas no certificado)", new ValidacaoPadrao(4), "Certificado de participação", ensino)
        );

        ensino.setAtividadesDisponiveis(atividades);
        return ensino;
    }

    private static Modalidade criarModalidadePesquisa() {
        Modalidade pesquisa = new Modalidade("Pesquisa e Inovação", 0.40);

        List<AtividadeComplementar> atividades = Arrays.asList(
                criarAtividade("Publicação de artigo em periódico", new ValidacaoHoraFixa(15), "Cópia do artigo", pesquisa),
                criarAtividade("Apresentação de trabalho em evento", new ValidacaoHoraFixa(10), "Certificado de apresentação", pesquisa)
        );

        pesquisa.setAtividadesDisponiveis(atividades);
        return pesquisa;
    }

    private static Modalidade criarModalidadeExtensao() {
        Modalidade extensao = new Modalidade("Extensão", 0.40);

        List<AtividadeComplementar> atividades = Arrays.asList(
                criarAtividade("Estágio não obrigatório na área", new ValidacaoPorMultiplicador(25, "mês"), "Contrato de estágio", extensao)
        );

        extensao.setAtividadesDisponiveis(atividades);
        return extensao;
    }

    private static Modalidade criarModalidadeComplementacao() {
        Modalidade complementacao = new Modalidade("Complementação", 0.20);

        List<AtividadeComplementar> atividades = Arrays.asList(
                criarAtividade("Participação em evento cultural", new ValidacaoHoraFixa(5), "Certificado ou ingresso", complementacao),
                criarAtividade("Curso de língua estrangeira", new ValidacaoHoraDeclaradaComLimite(40), "Certificado de conclusão", complementacao),
                criarAtividade("Trabalho voluntário", new ValidacaoHoraDeclaradaComLimite(30), "Declaração da instituição", complementacao)
        );

        complementacao.setAtividadesDisponiveis(atividades);
        return complementacao;
    }

    private static AtividadeComplementar criarAtividade(String descricao, Validacao estrategia, String docExigida, Modalidade modalidade) {
        return new AtividadeComplementar(descricao, estrategia, docExigida, modalidade);
    }
}
