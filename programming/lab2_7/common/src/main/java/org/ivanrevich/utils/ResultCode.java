package org.ivanrevich.utils;

/**
 * Перечисление результатов выполнения команды.
 * <p>
 * Объединяет коды результатов и строковые константы ошибок
 * (ранее находившиеся в классе Exceptions).
 * Используется для индикации успеха или неудачи выполнения команды,
 * а также для типизированной обработки ошибок в клиенте.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 */
public enum ResultCode {
    SUCCESS("Success"),
    FAIL_0("Fail"),
    INVALID_INPUT("Invalid input"),

    // Скрипты
    SCRIPT_END("Script finished"),
    SCRIPT_ERROR("Script error"),

    // Команды
    COMMAND_CANCELLED("Command cancelled"),
    COMMAND_SOFT_CANCELLED("Command soft cancelled"),

    COMMAND_NOT_FOUND("Command not found"),
    COMMAND_PARSE_ERROR("Command parse error"),

    // Скрипты
    RECURRENT_SCRIPT_ERROR("Recurrent script detected"),
    MANY_INCORRECT_ATTEMPTS("Too many incorrect attempts"),

    // Аргументы
    INVALID_NUM_OF_ARGS("Invalid number of arguments"),
    INVALID_ARGS("Invalid arguments"),

    // Данные
    ID_ISN_EXIST("Element with this id does not exist"),
    FILE_NOT_FOUND("File not found"),

    // Сеть
    INTERNAL_CLI_ERROR("Internal client error"),
    INVALID_REQUEST("Invalid request"),
    INTERNAL_SERVER_ERROR("Internal server error"),

    UNAUTHORIZED_REQUEST("User is not authorized"),
    HAVENT_OWNER_RULES("You don't have owner rules");

    private final String message;

    ResultCode(String message) {
        this.message = message;
    }

    public static ResultCode fromMessage(String message) {
        for (ResultCode code : values()) {
            if (code.message.equals(message)) {
                return code;
            }
        }

        throw new IllegalArgumentException("Unknown message: " + message);
    }
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}