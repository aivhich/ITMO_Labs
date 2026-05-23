package org.ivanrevich.managers;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.utils.ResultCode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.ivanrevich.validators.FileValidator.validateScriptFile;

/**
 * Реализация менеджера ввода-вывода для файлов.
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 */
public class FileIOManagerImpl implements IOManager {
    private final BufferedReader reader;
    private final String path;

    public FileIOManagerImpl(String path) {
        validateScriptFile(path);
        this.path = path;
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            throw new AppException(ResultCode.FILE_NOT_FOUND);
        }
    }

    @Override
    public String read() {
        System.out.print(">> ");
        try {
            String data = reader.readLine();
            if (data == null) {
                System.out.println();
                throw new AppException(ResultCode.SCRIPT_END);
            }
            System.out.println(data);
            return data;
        } catch (AppException e) {
            throw e;
        } catch (IOException e) {
            System.out.println();
            throw new AppException(ResultCode.SCRIPT_ERROR);
        }
    }

    @Override
    public void write(String text) {
        System.out.println(text);
    }

    @Override
    public String askString(String text) {
        write(text);
        return read();
    }

    @Override
    public Integer askInt(String text) {
        write(text);
        String s = read();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid integer");
        }
    }

    @Override
    public Float askFloat(String text) {
        write(text);
        String s = read().replace(',', '.');
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid float");
        }
    }

    @Override
    public Double askDouble(String text) {
        write(text);
        String s = read().replace(',', '.');
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid double");
        }
    }

    @Override
    public Long askLong(String text) {
        write(text);
        String s = read();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid long");
        }
    }

    @Override
    public <T> T askValue(T initValue, Supplier<T> input, Predicate<T> validator) {
        boolean isUpdateMode = initValue != null;
        T value = initValue;

        if (isUpdateMode) write(String.valueOf(value));

        T newValue = input.get();

        if (validator.test(newValue)) {
            return newValue;
        }

        if (isUpdateMode && newValue == null) return value;

        throw new IllegalArgumentException("Invalid script input");
    }

    @Override
    public String getFile() {
        return path;
    }
}