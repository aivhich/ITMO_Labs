package org.ivanrevich.commands;

import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.responses.Result;

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
    public Result<String> run(Request<?> r) {
        CommandManager commandManager = managersLocator.get(CommandManager.class);

        StringBuilder out = new StringBuilder("--- HELP ---\n");
        for(Command c: commandManager.getRegistedCommands()){
            out.append(c.toString()+"\n");
        }
        return new Result<>(ResultCode.SUCCESS, "Success", out.toString());
    }

    @Override
    public String toString() {
        return "help: display help on available commands";
    }
}
