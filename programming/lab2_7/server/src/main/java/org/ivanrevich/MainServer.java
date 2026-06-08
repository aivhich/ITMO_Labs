package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.config.AppConfig;
import org.ivanrevich.config.DataSourceConfig;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Server;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.TerminalConfigurator;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {
    private static final Logger logger = Logger.getLogger(MainServer.class.getName());
    private static final String dbUrl = AppConfig.getConfig("db.url","jdbc:postgresql://localhost:5432/proglab7/");
    private static final String dbUser = AppConfig.getConfig("db.user","postgres");
    private static final String dbPassword = AppConfig.getConfig("db.password","249120");

    public static void main(String[] args) throws Exception {
        AtomicBoolean collectionWasSaved = new AtomicBoolean(false);
        if (args.length < 1) {
            System.err.println("Change config: MainServer [port]");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        logger.log(Level.INFO, "Server is starting on port: " + port);

        DataSource dataSource = DataSourceConfig.create(dbUrl, dbUser, dbPassword);
        UserManager userManager = new UserManagerImpl(dataSource);

        QueueManager queueManager = new PsqlQueueManagerImpl(dataSource);
        StorageManager storageManager = new PsqlStorageManagerImpl(dataSource);

        TerminalConfigurator terminalConfigurator = new TerminalConfigurator();
        boolean isRawMode = TerminalConfigurator.enableRawMode();

        IOManager ioManager = new IOManagerImpl(isRawMode);
        IOManagerStack ioManagerStack = new IOManagerStack(ioManager);


        ManagersLocator managersLocator = new ManagersLocator();
        managersLocator.register(UserManager.class, userManager);
        managersLocator.register(IOManager.class, ioManager);
        managersLocator.register(IOManagerStack.class, ioManagerStack);
        managersLocator.register(StorageManager.class, storageManager);
        managersLocator.register(QueueManager.class, queueManager);

        CommandManager commandManager = new CommandManagerImpl(managersLocator);
        managersLocator.register(CommandManager.class, commandManager);

        commandManager.registerNoAuthCommands(Map.of(
                CommandType.LOGIN.getName(), new Login(managersLocator),
                CommandType.SIGNUP.getName(), new Signup(managersLocator)
        ));
        commandManager.registerCommands(
                Map.ofEntries(
                        Map.entry(CommandType.ADD.getName(), new Add(managersLocator)), //checked c-s
                        Map.entry(CommandType.CLEAR.getName(), new Clear(managersLocator)), // checked c-s
                        Map.entry(CommandType.COUNT_GREATER_THAN_FUEL_TYPE.getName(), new CountGreaterThanFuelType(managersLocator)), // checked c-s
                        Map.entry(CommandType.EXECUTE.getName(), new ExecuteScript(managersLocator)), // checked c-s
                        Map.entry(CommandType.EXIT.getName(), new Exit(managersLocator)), // checked c-s
                        Map.entry(CommandType.HELP.getName(), new Help(managersLocator)), // checked c-s
                        Map.entry(CommandType.HISTORY.getName(), new History(managersLocator)), // checked c-s
                        Map.entry(CommandType.INFO.getName(), new Info(managersLocator)), // checked c-s
                        Map.entry(CommandType.PRINT_ASCENDING.getName(), new PrintAscending(managersLocator)), // checked c-s
                        Map.entry(CommandType.PRINT_UNIQUE_FUEL_TYPE.getName(), new PrintUniqueFuelType(managersLocator)), // checked c-s
                        Map.entry(CommandType.REMOVE_BY_ID.getName(), new RemoveById(managersLocator)), // checked c-s
                        Map.entry(CommandType.REMOVE_HEAD.getName(), new RemoveHead(managersLocator)),  // checked c-s
                        Map.entry(CommandType.REMOVE_LOWER.getName(), new RemoveLower(managersLocator)), // checked c-s
                        Map.entry(CommandType.SAVE.getName(), new Save(managersLocator, "")), // checked c-s
                        Map.entry(CommandType.SHOW.getName(), new Show(managersLocator)), // checked c-s
                        Map.entry(CommandType.UPDATE.getName(), new Update(managersLocator)) // checked c-s
                )
        );
        Thread save = new Thread(() -> {
            logger.log(Level.INFO, "Shutting down the server - saving the collection");
            try {
                commandManager.run(new Request<>(CommandType.SAVE, new ArrayList()));
                collectionWasSaved.set(true);
                logger.log(Level.INFO, "Collection saved successfully");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error saving collection: " + e.getMessage());
            }
        });
        Runtime.getRuntime().addShutdownHook(save);

        logger.log(Level.INFO, "The server has been successfully initialized, waiting for connections...");

        Server server = new Server(port, managersLocator);
        managersLocator.register(Server.class, server);
        server.run();
        if(!collectionWasSaved.get()) {
            save.start();
        }
    }
}