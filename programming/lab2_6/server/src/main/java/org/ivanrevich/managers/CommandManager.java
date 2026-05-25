package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс менеджера команд сервера.
 * <p>
 * Принимает десериализованный {@link Request} от клиента,
 * находит нужную команду по {@link org.ivanrevich.requests.CommandType}
 * и возвращает {@link Result} для отправки обратно клиенту.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 * @see Command
 * @see CommandObj
 * @see CommandManagerImpl
 */
public interface CommandManager {

    /**
     * Парсит строку команды в объект {@link CommandObj}.
     * <p>
     * Используется только внутри сервера (например, для консольного управления).
     * Клиентские команды приходят уже в виде {@link Request}.
     * </p>
     *
     * @param cmd строка команды
     * @return объект с именем и аргументами
     * @throws AppException с кодом {@link ResultCode#COMMAND_PARSE_ERROR} если строка пустая
     */
    static CommandObj parseCommand(String cmd) {
        String[] tokens = cmd.trim().strip().split(" ");
        if (tokens.length > 0) {
            String command = tokens[0].toLowerCase();
            String[] args = new String[tokens.length - 1];
            if (tokens.length > 1) {
                System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
            }
            return new CommandObj(command, args);
        }
        throw new AppException(ResultCode.COMMAND_PARSE_ERROR);
    }

    /**
     * Возвращает историю выполненных команд.
     *
     * @return список объектов {@link CommandObj}
     */
    ArrayList<CommandObj> getHistory();

    /**
     * Выполняет команду из входящего запроса клиента.
     *
     * @param r десериализованный запрос от клиента
     * @return результат выполнения команды для отправки клиенту
     */
    Result<?> run(Request<?> r);

    /**
     * Возвращает все зарегистрированные команды.
     *
     * @return коллекция зарегистрированных команд
     */
    Collection<Command> getRegistedCommands();

    /**
     * Регистрирует набор команд.
     *
     * @param commands карта имя → команда
     */
    void registerCommands(Map<String, Command> commands);
}