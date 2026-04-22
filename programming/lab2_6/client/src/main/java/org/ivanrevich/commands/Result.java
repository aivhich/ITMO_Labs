package org.ivanrevich.commands;

/**
 * Перечисление результатов выполнения команды.
 * <p>
 * Используется для индикации успеха или неудачи выполнения команды.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public enum Result {
    /** Команда выполнена успешно */
    SUCCESS,

    /** При выполнении команды произошла ошибка */
    FAIL
}
