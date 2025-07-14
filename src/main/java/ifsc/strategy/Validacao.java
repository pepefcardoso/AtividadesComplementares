package ifsc.strategy;

public interface Validacao {
    public ResultadoValidacao validar(int horasDeclaradas);

    public String getDescricaoRegra();
}
