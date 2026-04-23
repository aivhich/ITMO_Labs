package org.ivanrevich.commands;

import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;


/**
 * Команда вывода справки по доступным командам.
 * <p>
 * Выводит описание всех зарегистрированных команд в приложении.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandManager
 */
public class Help implements Command{

    private final ManagersLocator managersLocator;

    public Help(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        CommandManager commandManager = managersLocator.get(CommandManager.class);

        ioManager.write("--- HELP ---");
        for(Command c: commandManager.getRegistedCommands()){
            ioManager.write(c.toString());
        }
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "help: display help on available commands";
    }
}
