package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.CommandObj;

import java.util.*;

/**
 * Реализация менеджера команд.
 * <p>
 * Хранит зарегистрированные команды, историю выполнения
 * и управляет их исполнением.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see CommandManager
 * @see ManagersLocator
 */
public class CommandManagerImpl implements CommandManager{
    private final HashMap<String, Command> availableCommands = new HashMap<String, Command>(); //TODO
    private final ArrayList<CommandObj> history = new ArrayList<>(); // Is it normal?

    @Override
    public ArrayList<CommandObj> getHistory() {
        return history;
    }

//    @Override
//    @Deprecated
//    public void run(String cmd) {
//        CommandObj parsedCommand = CommandManager.parseCommand(cmd);
//        if(availableCommands.containsKey(parsedCommand.name())){
//            if( ResultCode.SUCCESS != availableCommands.get(parsedCommand.name()).run(parsedCommand.args()).getResultCode()){
//                System.out.println("Command doesn't return success code");
//            }
//            history.add(parsedCommand);
//            return;
//        }
//        throw new RuntimeException(Exceptions.COMMAND_NOT_FOUND);
//    }

    @Override
    public Result<?> run(Request r) {
        Result resultCode = availableCommands.get(r.getCommandType().getName()).run(r);
        history.add(new CommandObj(r.getCommandType().getName(), new String[]{}));
        return resultCode;
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
