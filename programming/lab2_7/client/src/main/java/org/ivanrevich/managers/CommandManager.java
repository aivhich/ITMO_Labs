package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс менеджера команд клиента.
 * <p>
 * Управляет регистрацией, парсингом и выполнением команд.
 * Команды выполняются по строке, введённой пользователем.
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
     *
     * @param cmd строка команды от пользователя
     * @return объект с именем и аргументами команды
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
//
//    /**
//     * Возвращает историю выполненных команд.
//     *
//     * @return список объектов {@link CommandObj}
//     */
//    ArrayList<CommandObj> getHistory();

    /**
     * Выполняет команду по строке, введённой пользователем.
     *
     * @param cmd строка команды
     * @throws AppException с кодом {@link ResultCode#COMMAND_NOT_FOUND} если команда не зарегистрирована
     */
    void run(String cmd);

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