package org.ivanrevich;

public class ReflectionRepository<T> {

    private final Class<T> clazz;

    public ReflectionRepository(Class<T> clazz) {
        this.clazz = clazz;
    }
}