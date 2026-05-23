package org.ivanrevich.exceptions;

public class Exceptions {
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    private Exceptions() {
    }
    public static String  SCRIPT_END = "SCRIPT_END";
    public static String SCRIPT_ERROR = "SCRIPT_ERROR";

    public static String COMMAND_CANCELLED = "COMMAND_CANCELLED";
    public static String COMMAND_NOT_FOUND = "COMMAND_NOT_FOUND";
    public static String COMMAND_PARSE_ERROR = "COMMAND_PARSE_ERROR";

    public static String RECURRENT_SCRIPT_ERROR = "RECURRENT_SCRIPT_ERROR";
    public static String MANY_INCORRECT_ATTEMPTS = "MANY_INCORRECT_ATTEMPTS";

    public static String ID_ISN_EXIST = "ID_ISN_EXIST";
    public static String FILE_NOT_FOUND = "FILE_NOT_FOUND";
}
