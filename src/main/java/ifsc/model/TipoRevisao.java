package ifsc.model;

public enum TipoRevisao {
    MANTER_VALIDACAO(1),
    ALTERAR_HORAS(2),
    RECUSAR_ATIVIDADE(3);

    private final int valor;

    TipoRevisao(int valorOpcao) {
        this.valor = valorOpcao;
    }

    public int getValor() {
        return valor;
    }

    public static TipoRevisao fromValor(int valor) {
        for (TipoRevisao tipo : values()) {
            if (tipo.getValor() == valor) {
                return tipo;
            }
        }
        return MANTER_VALIDACAO;
    }
}
