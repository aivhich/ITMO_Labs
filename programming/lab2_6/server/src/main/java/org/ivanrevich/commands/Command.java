package org.ivanrevich.commands;


import org.ivanrevich.requests.Request;

/**
 * Интерфейс команды в паттерне Command.
 * <p>
 * Все команды в приложении реализуют этот интерфейс и могут быть выполнены
 * через метод {@link #run(String[])}.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see org.ivanrevich.responses.Result
 */
public interface Command{
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param args массив аргументов командной строки
     * @return {@link ResultCode#SUCCESS} если команда выполнена успешно,
     *         {@link ResultCode#FAIL} если произошла ошибка
     */

    org.ivanrevich.responses.Result<?> run(Request<?> r);

    org.ivanrevich.responses.Result<?>  run(String[] args);
}

