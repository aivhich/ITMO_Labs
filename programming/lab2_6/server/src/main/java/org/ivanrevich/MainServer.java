package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Server;

import java.util.Map;

public class MainServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);

        String path = args[1];

//        IOManager ioManager = new IOManagerImpl(false);
//        IOManagerStack ioStack = new IOManagerStack(ioManager);
        QueueManager queueManager = new QueueManagerImpl();
        StorageManager storageManager = new StorageManagerImpl(path, queueManager);

        ManagersLocator managersLocator = new ManagersLocator();
        managersLocator.register(StorageManager.class, storageManager);
//        managersLocator.register(IOManagerStack.class, ioStack);
//        managersLocator.register(IOManager.class, ioManager);
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
                        Map.entry("save", new Save(managersLocator, path)),
                        Map.entry("show", new Show(managersLocator)),
                        Map.entry("update", new Update(managersLocator))
                )
        );



        Server server = new Server(port, managersLocator);
        server.run();
    }
}