package org.ivanrevich.managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.network.ResponseHandler;
import org.ivanrevich.network.Server;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    private final HashMap<String, Command> publicCommands = new HashMap<>();
    private final ArrayList<CommandObj> history = new ArrayList<>();
    private ManagersLocator managersLocator;
    private final Logger logger = Logger.getLogger(Server.class.getName());

    public CommandManagerImpl(ManagersLocator managersLocator) {
        this.managersLocator=  managersLocator;
    }

    @Override
    public ArrayList<CommandObj> getHistory() {
        return history;
    }

    @Override
    public Result<?> run(Request<?> r) {
        UserManager userManager = managersLocator.get(UserManager.class);

        String commandName = r.getCommandType().getName();
        Command pubCommand = publicCommands.get(commandName);

        if(pubCommand == null) {
            /// BLOCK UNAUTHORIZED REQUEST
            if(!userManager.verify(r.getCredentials())){
                logger.log(Level.INFO, "Запрос от незарегистрировано пользователя");
                return new Result<>(ResultCode.UNAUTHORIZED_REQUEST, "You're not authorized to perform this operation.", "You're not authorized to perform this operation.");
            }
            Command command = availableCommands.get(commandName);
            if (command == null) throw new AppException(ResultCode.COMMAND_NOT_FOUND, commandName);
            Result<?> result = command.run(r);
            history.add(new CommandObj(commandName, new String[]{}));
            return result;
        }
        return pubCommand.run(r);
    }

    @Override
    public Result<?> run(String cmd) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        CommandObj parsedCommand = CommandManager.parseCommand(cmd);

        // TODO pub commands if we need????
        if(availableCommands.containsKey(parsedCommand.name())){
            Result<?> r = availableCommands.get(parsedCommand.name()).run(parsedCommand.args());
            if( ResultCode.SUCCESS != r.getResultCode()){
                ioManager.write(r.getResultCode().toString());
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
    public void registerNoAuthCommands(Map<String, Command> commands) {
        publicCommands.putAll(commands);
    }

    @Override
    public Collection<Command> getRegistedCommands() {
        return availableCommands.values();
    }

    @Override
    public Collection<Command> getPubCommands() {
        return publicCommands.values();
    }
}