package org.ivanrevich.commands;


import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

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
     * @return {@link Response}
     */

    ResultCode run(String[] args);

//
//    @Deprecated
//    Result<?> run(String[] args);
}

