package org.ivanrevich.requests;

public enum CommandType {
    ADD("add"),
    CLEAR("clear"),
    COUNT_GREATER_THAN_FUEL_TYPE("count_greater_than_fuel_type"),
    EXECUTE("execute_script"),
    EXIT("exit"),
    HELP("help"),
    HISTORY("history"),
    INFO("info"),
    PRINT_ASCENDING("print_ascending"),
    PRINT_UNIQUE_FUEL_TYPE("print_unique_fuel_type"),
    REMOVE_BY_ID("remove_by_id"),
    REMOVE_HEAD("remove_head"),
    REMOVE_LOWER("remove_lower"),
    SAVE("save"),
    SHOW("show"),
    UPDATE("update"),

    LOGIN("login"),
    SIGNUP("signup");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
