package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Server;
import org.ivanrevich.requests.CommandType;

import java.util.Map;

public class MainServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);

        String path = args[1];

        QueueManager queueManager = new QueueManagerImpl();
        StorageManager storageManager = new StorageManagerImpl(path, queueManager);

        ManagersLocator managersLocator = new ManagersLocator();
        managersLocator.register(StorageManager.class, storageManager);
        managersLocator.register(QueueManager.class, queueManager);


        CommandManager commandManager = new CommandManagerImpl();
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
                        Map.entry(CommandType.SAVE.getName(), new Save(managersLocator, path)),
                        Map.entry(CommandType.SHOW.getName(), new Show(managersLocator)),
                        Map.entry(CommandType.UPDATE.getName(), new Update(managersLocator))
                )
        );



        Server server = new Server(port, managersLocator);
        server.run();
    }
}