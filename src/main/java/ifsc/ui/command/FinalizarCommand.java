package ifsc.ui.command;

import ifsc.ui.ConsoleUI;

public class FinalizarCommand implements MenuCommand {

    private final ConsoleUI ui;

    public FinalizarCommand(ConsoleUI ui) {
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.finalizarAvaliacao();
    }
}
