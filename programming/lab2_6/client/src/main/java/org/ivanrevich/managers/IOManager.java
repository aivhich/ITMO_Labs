package org.ivanrevich.managers;


import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Интерфейс менеджера ввода-вывода.
 * <p>
 * Абстрагирует операции ввода и вывода, позволяя переключаться
 * между консольным и файловым вводом.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see IOManagerImpl
 * @see FileIOManagerImpl
 */

public interface IOManager {
    String getFile();
    String read();
    void write(String text);

    String askString(String text);

    Long askLong(String text);

    Double askDouble(String text);

    Float askFloat(String text);

    Integer askInt(String text);

    <T> T askValue(T initValue,
                           Supplier<T> input,
                           Predicate<T> validator);
}
