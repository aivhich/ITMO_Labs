package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.exceptions.ErrorHandler;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        AtomicBoolean workMode = new AtomicBoolean(true);

        ManagersLocator managersLocator = new ManagersLocator();


        Client client = new Client(new InetSocketAddress(args[0], Integer.parseInt(args[1])), managersLocator);
        IOManager ioManager = new IOManagerImpl(false);
        IOManagerStack ioStack = new IOManagerStack(ioManager);

        AuthManager authManager = new AuthManagerImpl(managersLocator);
        CommandManager commandManager = new CommandManagerImpl();

        managersLocator.register(AuthManager.class, authManager);
        managersLocator.register(IOManagerStack.class, ioStack);
        managersLocator.register(IOManager.class, ioManager);
        managersLocator.register(Client.class, client);
        managersLocator.register(CommandManager.class, commandManager);

        ErrorHandler errorHandler = new ClientErrorHandler(managersLocator, ioManager, workMode);

        commandManager.registerNoAuthCommands(Map.of(
                CommandType.SIGNUP.getName(), new Signup(managersLocator),
                CommandType.LOGIN.getName(), new Login(managersLocator)
        ));
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

        while (workMode.get()) {
            try {
                IOManager io = managersLocator.get(IOManager.class);
                String cmd = io.read();

                if (cmd == null) break;
                if (cmd.isEmpty()) continue;

                commandManager.run(cmd);

            } catch (AppException e) {
//                System.out.println(e.getMessage());
                errorHandler.handle(e);
            } catch (RuntimeException e) {
                String msg = e.getMessage();
                ioManager.write(msg != null ? msg : "Неизвестная ошибка");
            }
        }

        client.close();
        System.out.println("Клиент завершил работу.");
    }
}