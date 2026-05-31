package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация менеджера команд клиента.
 * <p>
 * Парсит строку от пользователя, находит нужную команду,
 * выполняет её и сохраняет в историю.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 * @see CommandManager
 */
public class CommandManagerImpl implements CommandManager {
    private final HashMap<String, Command> availableCommands = new HashMap<>();
//    private final ArrayList<CommandObj> history = new ArrayList<>();
//
//    @Override
//    public ArrayList<CommandObj> getHistory() {
//        return history;
//    }

    @Override
    public void run(String cmd) {
        CommandObj parsedCommand = CommandManager.parseCommand(cmd);

        if (!availableCommands.containsKey(parsedCommand.name())) {
            throw new AppException(ResultCode.COMMAND_NOT_FOUND);
        }

        ResultCode result = availableCommands.get(parsedCommand.name()).run(parsedCommand.args());

        if (result != ResultCode.SUCCESS) {
            System.out.println("[" + result.name() + "] " + result.getMessage());
        }

        //history.add(parsedCommand);
    }

    @Override
    public void registerCommands(Map<String, Command> commands) {
        availableCommands.putAll(commands);
    }

    @Override
    public Collection<Command> getRegistedCommands() {
        return availableCommands.values();
    }
}