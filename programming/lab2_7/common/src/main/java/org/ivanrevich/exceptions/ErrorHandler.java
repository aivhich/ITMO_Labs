package org.ivanrevich.exceptions;

import org.ivanrevich.utils.ResultCode;
import java.util.function.Consumer;

public abstract class ErrorHandler {

    protected Runnable onScriptEnd;
    protected Runnable onScriptError;
    protected Runnable onCommandCancelled;
    protected Runnable onCommandSoftCancelled;
    protected Runnable onUnauthorizedRequest;

    protected Consumer<String> messageWriter;

    /**
     * Конструктор принимает действия, которые будут вызваны при соответствующих ошибках.
     * @param messageWriter           вывод сообщений пользователю
     * @param onScriptEnd             действие при SCRIPT_END
     * @param onScriptError           действие при SCRIPT_ERROR
     * @param onCommandCancelled      действие при COMMAND_CANCELLED
     * @param onCommandSoftCancelled  действие при COMMAND_SOFT_CANCELLED (может быть null)
     */
    public ErrorHandler(Consumer<String> messageWriter,
                        Runnable onScriptEnd,
                        Runnable onScriptError,
                        Runnable onCommandCancelled,
                        Runnable onCommandSoftCancelled,
                        Runnable onUnauthorizedRequest) {
        this.messageWriter = messageWriter;
        this.onScriptEnd = onScriptEnd;
        this.onScriptError = onScriptError;
        this.onCommandCancelled = onCommandCancelled;
        this.onCommandSoftCancelled = onCommandSoftCancelled;
        this.onUnauthorizedRequest = onUnauthorizedRequest;
    }

    public void handle(AppException e) {
        ResultCode code = e.getCode();
        switch (code) {
            case SCRIPT_END:
                onScriptEnd.run();
                break;
            case SCRIPT_ERROR:
                onScriptError.run();
                break;
            case COMMAND_CANCELLED:
                onCommandCancelled.run();
                break;
            case COMMAND_SOFT_CANCELLED:
                if (onCommandSoftCancelled != null) {
                    onCommandSoftCancelled.run();
                }
                break;
            case COMMAND_NOT_FOUND:
                messageWriter.accept(getCommandNotFoundMessage());
                break;
            case COMMAND_PARSE_ERROR:
                messageWriter.accept(getCommandParseErrorMessage());
                break;
            case RECURRENT_SCRIPT_ERROR:
                messageWriter.accept(getRecursiveScriptMessage());
                break;
            case MANY_INCORRECT_ATTEMPTS:
                messageWriter.accept(getManyIncorrectAttemptsMessage());
                break;
            case ID_ISN_EXIST:
                messageWriter.accept(getIdNotExistMessage());
                break;
            case FILE_NOT_FOUND:
                messageWriter.accept(getFileNotFoundMessage());
                break;
            case INVALID_NUM_OF_ARGS:
                messageWriter.accept(getInvalidNumberOfArgsMessage());
                break;
            case INVALID_ARGS:
                messageWriter.accept(getInvalidArgsMessage());
                break;
            case UNAUTHORIZED_REQUEST:
                onUnauthorizedRequest.run();
                break;
            default:
                messageWriter.accept(e.getMessage() != null ? e.getMessage() : "Unknown error");
        }
    }

    protected String getCommandNotFoundMessage() {
        return "Command not found. Type 'help' for list of commands.";
    }
    protected String getCommandParseErrorMessage() {
        return "Command parse error.";
    }
    protected String getRecursiveScriptMessage() {
        return "Recursive script detected. Execution stopped.";
    }
    protected String getManyIncorrectAttemptsMessage() {
        return "Too many incorrect input attempts.";
    }
    protected String getIdNotExistMessage() {
        return "Element with such ID does not exist.";
    }
    protected String getFileNotFoundMessage() {
        return "File unreachable. Check path and permissions.";
    }
    protected String getInvalidNumberOfArgsMessage() {
        return "Invalid number of arguments.";
    }
    protected String getInvalidArgsMessage() {
        return "Invalid arguments.";
    }
}