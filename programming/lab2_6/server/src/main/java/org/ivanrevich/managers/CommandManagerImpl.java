package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация менеджера команд сервера.
 * <p>
 * Принимает {@link Request} от клиента, извлекает {@link org.ivanrevich.requests.CommandType},
 * находит соответствующую команду, выполняет её и возвращает {@link Result}.
 * Также ведёт историю выполненных команд.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 * @see CommandManager
 */
public class CommandManagerImpl implements CommandManager {
    private final HashMap<String, Command> availableCommands = new HashMap<>();
    private final ArrayList<CommandObj> history = new ArrayList<>();

    @Override
    public ArrayList<CommandObj> getHistory() {
        return history;
    }

    @Override
    public Result<?> run(Request<?> r) {
        String commandName = r.getCommandType().getName();

        Command command = availableCommands.get(commandName);
        if (command == null) {
            throw new AppException(ResultCode.COMMAND_NOT_FOUND, commandName);
        }

        Result<?> result = command.run(r);

        history.add(new CommandObj(commandName, new String[]{}));

        return result;
    }

    @Override
    public Result<?> run(String cmd) {
        CommandObj parsedCommand = CommandManager.parseCommand(cmd);
        if(availableCommands.containsKey(parsedCommand.name())){
            Result<?> r = availableCommands.get(parsedCommand.name()).run(parsedCommand.args());
            if( ResultCode.SUCCESS != r.getResultCode()){
                System.out.println("Command doesn't return success code");
            }
            history.add(parsedCommand);
            return r;
        }
        throw new AppException(ResultCode.COMMAND_NOT_FOUND);
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