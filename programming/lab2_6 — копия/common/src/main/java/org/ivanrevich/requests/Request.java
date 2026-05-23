package org.ivanrevich.requests;

import java.io.Serializable;
import java.util.List;

public class Request<T> implements Serializable {
    private final CommandType commandType;
    private final T args;

    public Request(CommandType commandType, T args) {
        this.commandType = commandType;
        this.args = args;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public T getArgs() {
        return args;
    }
}
