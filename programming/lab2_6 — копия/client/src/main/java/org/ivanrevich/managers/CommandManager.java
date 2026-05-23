package org.ivanrevich.managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.utils.CommandObj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * Интерфейс менеджера команд.
 * <p>
 * Управляет регистрацией, парсингом и выполнением команд в приложении.
 * Реализует паттерн Command Dispatcher.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandObj
 */
public interface CommandManager {
    /**
     * Парсит строку команды в объект CommandObj.
     *
     * @param cmd строка команды от пользователя
     * @return объект {@link CommandObj} с именем и аргументами
     * @throws RuntimeException если команда не может быть распарсена
     */
    static CommandObj parseCommand(String cmd){
        String[] tokens = cmd.trim().strip().split(" ");
        if(tokens.length>0){
            String command = tokens[0].toLowerCase();
            String args[] = new String[tokens.length-1];

            if(tokens.length>1){
                System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
            }
            return new CommandObj(command, args);
        }
        throw new RuntimeException(Exceptions.COMMAND_PARSE_ERROR);
    };

    /**
     * Возвращает историю выполненных команд.
     *
     * @return список объектов CommandObj
     */
    ArrayList<CommandObj> getHistory();


//    void run(String cmd);
    /**
     * Выполняет команду по строке.
     *
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
     * @param commands карта имя-команда для регистрации
     */
    void registerCommands(Map<String, Command> commands);
}
