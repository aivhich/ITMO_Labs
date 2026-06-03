package org.ivanrevich.requests;

import org.ivanrevich.auth.Credentials;

import java.io.Serializable;

public class Request<T> implements Serializable {
    private final CommandType commandType;
    private final T args;
    private Credentials credentials;

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
