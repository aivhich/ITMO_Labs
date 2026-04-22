package org.ivanrevich.exceptions;

public class Exceptions {
    private Exceptions() {
    }

    public static final String SCRIPT_END = "SCRIPT_END";
    public static final String SCRIPT_ERROR = "SCRIPT_ERROR";
    public static final String COMMAND_CANCELLED = "COMMAND_CANCELLED";
    public static final String COMMAND_SOFT_CANCELLED = "COMMAND_SOFT_CANCELLED";
    public static final String COMMAND_NOT_FOUND = "COMMAND_NOT_FOUND";
    public static final String COMMAND_PARSE_ERROR = "COMMAND_PARSE_ERROR";
    public static final String RECURRENT_SCRIPT_ERROR = "RECURRENT_SCRIPT_ERROR";
    public static final String MANY_INCORRECT_ATTEMPTS = "MANY_INCORRECT_ATTEMPTS";
    public static final String INVALID_NUM_OF_ARGS = "INVALID_NUM_OF_ARGS";
    public static final String INVALID_ARGS = "INVALID_ARGS";
    public static final String ID_ISN_EXIST = "ID_ISN_EXIST";

    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
}
