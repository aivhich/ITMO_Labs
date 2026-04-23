package org.ivanrevich.requests;

public enum CommandType {
    ADD("add"),
    INFO("info"),
    HISTORY("history"),
    UPDATE("update");

    private String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
