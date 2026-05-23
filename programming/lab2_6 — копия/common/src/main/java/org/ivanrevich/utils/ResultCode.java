package org.ivanrevich.utils;

/**
 * Перечисление результатов выполнения команды.
 * <p>
 * Используется для индикации успеха или неудачи выполнения команды.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
//TODO CLEANUP IT
public enum ResultCode{
    SUCCESS,
    INVALID_INPUT,
    SCRIPT_END,
    //SCRIPT_ERROR,
    //COMMAND_CANCELLED,
    //COMMAND_SOFT_CANCELLED,
    //COMMAND_NOT_FOUND,
    //COMMAND_PARSE_ERROR,
    RECURRENT_SCRIPT_ERROR,
    MANY_INCORRECT_ATTEMPTS,
    INVALID_NUM_OF_ARGS,
    INVALID_ARGS,
    ID_ISN_EXIST,
    FILE_NOT_FOUND,
    INTERNAL_CLI_ERROR,
    INVALID_REQUEST,
    INTERNAL_SERVER_ERROR;

}
