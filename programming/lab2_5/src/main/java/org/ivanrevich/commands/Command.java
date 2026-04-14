package org.ivanrevich.commands;


/**
 * Интерфейс команды в паттерне Command.
 * <p>
 * Все команды в приложении реализуют этот интерфейс и могут быть выполнены
 * через метод {@link #run(String[])}.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Result
 */
public interface Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param args массив аргументов командной строки
     * @return {@link Result#SUCCESS} если команда выполнена успешно,
     *         {@link Result#FAIL} если произошла ошибка
     */
    Result run(String[] args);
}

