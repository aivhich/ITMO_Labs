package org.ivanrevich.managers;

import org.ivanrevich.exceptions.Exceptions;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Реализация менеджера ввода-вывода для консоли.
 * <p>
 * Обрабатывает ввод с клавиатуры с поддержкой backspace,
 * raw-режима и обработки Ctrl+D.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see IOManager
 */
public class IOManagerImpl implements IOManager {
    private boolean isRawMode = false;
    public IOManagerImpl(boolean isRawMode) {
        this.isRawMode = isRawMode;
    }

    @Override
    public String read() {
        if(isRawMode)System.out.print("\r>> ");
        else System.out.print(">> ");
        StringBuilder buffer = new StringBuilder();

        try {
            while (true) {
                int ch = System.in.read();

                // EOF (Ctrl+D)
                if (ch == -1 || ch == 4) {
                    if(isRawMode)System.out.println("^D");
                    throw new RuntimeException(Exceptions.COMMAND_CANCELLED);
                }

                if(ch==3){
                    if(isRawMode)System.out.println("^C");
                    throw new RuntimeException(Exceptions.COMMAND_CANCELLED);
                }

                // Enter
                if (ch == '\n' || ch == '\r') {
                    if(isRawMode)System.out.print("\r\n");
                    return buffer.toString().trim();
                }

                // Backspace
                if (ch == 127 || ch == 8) {
                    if (!buffer.isEmpty()) {
                        buffer.deleteCharAt(buffer.length() - 1);
                        System.out.print("\b \b");
                    }
                    continue;
                }

                buffer.append((char) ch);
                if(isRawMode)System.out.print((char) ch);
            }

        } catch (IOException e) {
            throw new RuntimeException("INPUT_ERROR");
        }
    }

    @Override
    public void write(String text) {
        if(isRawMode) System.out.print("\r");
        System.out.println(text);
    }

    @Override
    public String askString(String text) {
        write(text);
        return read();
    }

    @Override
    public Integer askInt(String text) {
        String s = askString(text);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if(s.isBlank()) {
                return null;
            }
            throw new RuntimeException("Value is not a valid integer");
        }
    }

    @Override
    public Float askFloat(String text) {
        String s = askString(text).replace(',', '.');
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            if(s.isBlank()) {
                return null;
            }
            throw new RuntimeException("Value is not a valid float");
        }
    }

    @Override
    public Double askDouble(String text) {
        String s = askString(text).replace(',', '.');
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            if(s.isBlank()) {
                return null;
            }
            throw new RuntimeException("Value is not a valid double");
        }
    }

    @Override
    public Long askLong(String text) {
        String s = askString(text);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            if(s.isBlank()) {
                return null;
            }
            throw new RuntimeException("Value is not a valid long");
        }
    }

    @Override
    public <T> T askValue(T initValue, Supplier<T> input, Predicate<T> validator) {
        boolean isUpdateMode = initValue != null;
        T value = initValue;

        for (int i = 0; i < 3; i++) {
            if (isUpdateMode) write(String.valueOf(value));

            try {
                T newValue = input.get();
                if (validator.test(newValue)) {
                    if (isUpdateMode && (newValue == null || newValue.toString().isEmpty())) return value; // here was upd mistake

                    value = newValue;
                    break;
                }

                if (isUpdateMode && (newValue == null || newValue.toString().isEmpty())) return value;//here was upd mistake
            }catch (Exception e){
                if(Exceptions.COMMAND_CANCELLED.equals(e.getMessage()))  throw new RuntimeException(Exceptions.COMMAND_CANCELLED);
                write(e.getMessage());
            }
            if (i < 2) {
                write("Incorrect data, please reenter data");
            } else {
                throw new IllegalArgumentException(Exceptions.MANY_INCORRECT_ATTEMPTS);
            }
        }

        return value;
    }

    @Override
    public String getFile() {
        return "IO";
    }
}