package org.ivanrevich.requests;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private CommandType commandType;
    private List<Object> args;

    public Request(CommandType commandType, List<Object> args) {
        this.commandType = commandType;
        this.args = args;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public List<Object> getArgs() {
        return args;
    }
}
