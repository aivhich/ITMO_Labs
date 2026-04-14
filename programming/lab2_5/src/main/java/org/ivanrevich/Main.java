package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.managers.*;
import org.ivanrevich.utils.TerminalConfigurator;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if(args.length!=1){
            System.out.println("Please add path to file with a collection as argument for a program");
            System.exit(0);
        }

        TerminalConfigurator terminalConfigurator = new TerminalConfigurator();
        boolean isRawMode = TerminalConfigurator.enableRawMode();
        boolean workMode = true;

        IOManager ioManager = new IOManagerImpl(isRawMode);
        IOManagerStack ioStack = new IOManagerStack(ioManager);
        QueueManager queueManager = new QueueManagerImpl();
        StorageManager storageManager = new StorageManagerImpl(args[0], queueManager);

        ManagersLocator managersLocator = new ManagersLocator();

        managersLocator.register(StorageManager.class, storageManager);
        managersLocator.register(IOManagerStack.class, ioStack);
        managersLocator.register(IOManager.class, ioManager);
        managersLocator.register(QueueManager.class, queueManager);


        CommandManager commandManager = new CommandManagerImpl();
        managersLocator.register(CommandManager.class, commandManager);

        commandManager.registerCommands(
                Map.ofEntries(
                        Map.entry("add", new Add(managersLocator)),
                        Map.entry("clear", new Clear(managersLocator)),
                        Map.entry("count_greater_than_fuel_type", new CountGreaterThanFuelType(managersLocator)),
                        Map.entry("execute_script", new ExecuteScript(managersLocator)),
                        Map.entry("exit", new Exit(managersLocator)),
                        Map.entry("help", new Help(managersLocator)),
                        Map.entry("history", new History(managersLocator)),
                        Map.entry("info", new Info(managersLocator)),
                        Map.entry("print_ascending", new PrintAscending(managersLocator)),
                        Map.entry("print_unique_fuel_type", new PrintUniqueFuelType(managersLocator)),
                        Map.entry("remove_by_id", new RemoveById(managersLocator)),
                        Map.entry("remove_head", new RemoveHead(managersLocator)),
                        Map.entry("remove_lower", new RemoveLower(managersLocator)),
                        Map.entry("save", new Save(managersLocator, args[0])),
                        Map.entry("show", new Show(managersLocator)),
                        Map.entry("update", new Update(managersLocator))
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
                    case Exceptions.SCRIPT_END -> {
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
                    case Exceptions.COMMAND_CANCELLED, Exceptions.COMMAND_SOFT_CANCELLED ->{
                        if(isRawMode) ioManager.write("Command cancelled");
                        else workMode=false;
                    }
                    case Exceptions.COMMAND_NOT_FOUND -> ioManager.write("Such command not found");
                    case Exceptions.COMMAND_PARSE_ERROR -> ioManager.write("Command parse error");
                    case Exceptions.RECURRENT_SCRIPT_ERROR -> ioManager.write("You're trying to start recurrent scripts");
                    case Exceptions.MANY_INCORRECT_ATTEMPTS -> ioManager.write("You're trying to enter incorrect data so many times");
                    case Exceptions.INVALID_NUM_OF_ARGS ->  ioManager.write("Invalid number of arguments");
                    case Exceptions.INVALID_ARGS -> ioManager.write("Invalid arguments");
                    case Exceptions.ID_ISN_EXIST -> ioManager.write("Element with such id is not exists");
                    case Exceptions.FILE_NOT_FOUND -> ioManager.write("File unreachable. Check file permission and path");
                    default -> ioManager.write(e.getMessage());
                }
            }
        }
    }
}
