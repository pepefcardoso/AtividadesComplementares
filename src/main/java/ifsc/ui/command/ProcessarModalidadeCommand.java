package ifsc.ui.command;

import ifsc.ui.ConsoleUI;

public class ProcessarModalidadeCommand implements MenuCommand {

    private final ConsoleUI ui;
    private final int tipoModalidade;

    public ProcessarModalidadeCommand(ConsoleUI ui, int tipoModalidade) {
        this.ui = ui;
        this.tipoModalidade = tipoModalidade;
    }

    @Override
    public void execute() {
        ui.processarModalidade(tipoModalidade);
    }
}
