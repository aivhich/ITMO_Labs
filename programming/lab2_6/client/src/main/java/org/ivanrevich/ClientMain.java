package org.ivanrevich;

import org.ivanrevich.commands.*;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.*;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.utils.ResultCode;

import java.net.InetSocketAddress;
import java.util.Map;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Client client = new Client(new InetSocketAddress("localhost", 8000));
        boolean workMode = true;

        ManagersLocator managersLocator = new ManagersLocator();
        IOManager ioManager = new IOManagerImpl(false);
        IOManagerStack ioStack = new IOManagerStack(ioManager);
        CommandManager commandManager = new CommandManagerImpl();

        managersLocator.register(IOManagerStack.class, ioStack);
        managersLocator.register(IOManager.class, ioManager);
        managersLocator.register(Client.class, client);
        managersLocator.register(CommandManager.class, commandManager);

        // Команда save убрана из клиента согласно заданию
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

        while (workMode) {
            try {
                IOManager io = managersLocator.get(IOManager.class);
                String cmd = io.read();

                if (cmd == null) break;
                if (cmd.isEmpty()) continue;

                commandManager.run(cmd);

            } catch (AppException e) {
                // Типизированные исключения приложения — switch по enum без проблем с константами
                switch (e.getCode()) {
                    case SCRIPT_END -> {
                        IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                        stack.pop();
                        managersLocator.register(IOManager.class, stack.current());
                    }
                    case SCRIPT_ERROR -> {
                        IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                        stack.pop();
                        managersLocator.register(IOManager.class, stack.current());
                        ioManager.write("Ошибка выполнения скрипта");
                    }
                    case COMMAND_CANCELLED -> workMode = false;
                    case COMMAND_NOT_FOUND ->
                            ioManager.write("Команда не найдена. Введите 'help' для списка команд.");
                    case COMMAND_PARSE_ERROR ->
                            ioManager.write("Ошибка разбора команды");
                    case RECURRENT_SCRIPT_ERROR ->
                            ioManager.write("Обнаружена рекурсия скриптов");
                    case MANY_INCORRECT_ATTEMPTS ->
                            ioManager.write("Слишком много некорректных попыток ввода");
                    case ID_ISN_EXIST ->
                            ioManager.write("Элемент с таким id не существует");
                    case FILE_NOT_FOUND ->
                            ioManager.write("Файл недоступен. Проверьте путь и права доступа");
                    default ->
                            ioManager.write(e.getMessage());
                }
            } catch (RuntimeException e) {
                String msg = e.getMessage();
                ioManager.write(msg != null ? msg : "Неизвестная ошибка");
            }
        }

        client.close();
        System.out.println("Клиент завершил работу.");
    }
}