package org.ivanrevich.managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
 * @see org.ivanrevich.ManagersLocator
 */
public class CommandManagerImpl implements CommandManager{
    private final HashMap<String, Command> availableCommands = new HashMap<String, Command>(); //TODO
    private final ArrayList<CommandObj> history = new ArrayList<>(); // Is it normal?
    private final ManagersLocator managersLocator;


    public CommandManagerImpl(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ArrayList<CommandObj> getHistory() {
        return history;
    }

    @Override
    public void run(String cmd) {
        CommandObj parsedCommand = CommandManager.parseCommand(cmd);
        IOManager ioManager = managersLocator.get(IOManager.class);
        if(availableCommands.containsKey(parsedCommand.name())){
            ResultCode rCode =  availableCommands.get(parsedCommand.name()).run(parsedCommand.args());
            switch (rCode) {
                case SUCCESS ->{
                    history.add(parsedCommand);
                    ioManager.write(rCode.name());
                }
                case SCRIPT_END -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    managersLocator.register(IOManager.class, stack.current());
                }
                case Exceptions.SCRIPT_ERROR -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    ioManager.write("");
                    managersLocator.register(IOManager.class, stack.current());
                    ioManager.write("Script execute error");
                }
//                case Exceptions.COMMAND_CANCELLED, Exceptions.COMMAND_SOFT_CANCELLED -> {
//                    if (isRawMode) ioManager.write("Command cancelled");
//                    else workMode = false;
//                }
                case Exceptions.COMMAND_NOT_FOUND -> ioManager.write("Such command not found");
                case Exceptions.COMMAND_PARSE_ERROR -> ioManager.write("Command parse error");
                case Exceptions.RECURRENT_SCRIPT_ERROR -> ioManager.write("You're trying to start recurrent scripts");
                case Exceptions.MANY_INCORRECT_ATTEMPTS ->
                        ioManager.write("You're trying to enter incorrect data so many times");
//                case Exceptions.INVALID_NUM_OF_ARGS -> ioManager.write("Invalid number of arguments");
//                case Exceptions.INVALID_ARGS -> ioManager.write("Invalid arguments");
                case Exceptions.ID_ISN_EXIST -> ioManager.write("Element with such id is not exists");
                case Exceptions.FILE_NOT_FOUND -> ioManager.write("File unreachable. Check file permission and path");
            }
            // TODO ERROR PRINT AND HANDLE
            return;
        }
        throw new RuntimeException(Exceptions.COMMAND_NOT_FOUND);
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
