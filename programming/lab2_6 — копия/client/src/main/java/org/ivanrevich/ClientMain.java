package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;

import java.net.InetSocketAddress;
import java.util.Map;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Client client = new Client(new InetSocketAddress("localhost", 8000));
        Boolean workMode = true;
        Boolean isRawMode = false;
        ManagersLocator managersLocator = new ManagersLocator();
        IOManager ioManager = new IOManagerImpl(false);
        IOManagerStack ioStack = new IOManagerStack(ioManager);
        CommandManager commandManager = new CommandManagerImpl(managersLocator);

        managersLocator.register(IOManagerStack.class, ioStack);
        managersLocator.register(IOManager.class, ioManager);
        managersLocator.register(Client.class, client);
        managersLocator.register(CommandManager.class, commandManager);

        commandManager.registerCommands(
                Map.ofEntries(
                        Map.entry(CommandType.ADD.getName(), new Add(managersLocator)),
                        Map.entry(CommandType.CLEAR.getName(), new Clear(managersLocator)),
                        Map.entry(CommandType.COUNT_GREATER_THAN_FUEL_TYPE.getName(), new CountGreaterThanFuelType(managersLocator)),
                        Map.entry(CommandType.EXECUTE.getName(), new ExecuteScript(managersLocator)),
                        Map.entry(CommandType.EXIT.getName(), new Exit(managersLocator)),
                        Map.entry(CommandType.HELP.getName(), new Help(managersLocator)),
                        Map.entry(CommandType.HISTORY.getName(), new History(managersLocator)),
                        Map.entry(CommandType.INFO.getName(), new Info(managersLocator)),
                        Map.entry(CommandType.PRINT_ASCENDING.getName(), new PrintAscending(managersLocator)),
                        Map.entry(CommandType.PRINT_UNIQUE_FUEL_TYPE.getName(), new PrintUniqueFuelType(managersLocator)),
                        Map.entry(CommandType.REMOVE_BY_ID.getName(), new RemoveById(managersLocator)),
                        Map.entry(CommandType.REMOVE_HEAD.getName(), new RemoveHead(managersLocator)),
                        Map.entry(CommandType.REMOVE_LOWER.getName(), new RemoveLower(managersLocator)),
                        Map.entry(CommandType.SHOW.getName(), new Show(managersLocator)),
                        Map.entry(CommandType.UPDATE.getName(), new Update(managersLocator))
                )
        );

        while(workMode) {
            try {
                IOManager io = managersLocator.get(IOManager.class);
                String cmd = io.read();

                if (cmd == null) break;

                if (cmd.isEmpty()) continue;

                commandManager.run(cmd);
            } catch (RuntimeException e) {
                switch (e.getMessage()) {
//                    case Exceptions.SCRIPT_END -> {
//                        IOManagerStack stack = managersLocator.get(IOManagerStack.class);
//                        stack.pop();
//                        managersLocator.register(IOManager.class, stack.current());
//                    }
//                    case Exceptions.SCRIPT_ERROR -> {
//                        IOManagerStack stack = managersLocator.get(IOManagerStack.class);
//                        stack.pop();
//                        ioManager.write("");
//                        managersLocator.register(IOManager.class, stack.current());
//                        ioManager.write("Script execute error");
//                    }
//                    case Exceptions.COMMAND_CANCELLED, Exceptions.COMMAND_SOFT_CANCELLED ->{
//                        if(isRawMode) ioManager.write("Command cancelled");
//                        else workMode=false;
//                    }
//                    case Exceptions.COMMAND_NOT_FOUND -> ioManager.write("Such command not found");
//                    case Exceptions.COMMAND_PARSE_ERROR -> ioManager.write("Command parse error");
//                    case Exceptions.RECURRENT_SCRIPT_ERROR -> ioManager.write("You're trying to start recurrent scripts");
//                    case Exceptions.MANY_INCORRECT_ATTEMPTS -> ioManager.write("You're trying to enter incorrect data so many times");
//                    case Exceptions.INVALID_NUM_OF_ARGS ->  ioManager.write("Invalid number of arguments");
//                    case Exceptions.INVALID_ARGS -> ioManager.write("Invalid arguments");
//                    case Exceptions.ID_ISN_EXIST -> ioManager.write("Element with such id is not exists");
//                    case Exceptions.FILE_NOT_FOUND -> ioManager.write("File unreachable. Check file permission and path");
                    default -> ioManager.write(e.getMessage());
                }
            }
        }
    }
}