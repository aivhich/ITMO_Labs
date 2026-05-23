package org.ivanrevich.managers;

import org.ivanrevich.exceptions.Exceptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.ivanrevich.validators.FileValidator.validateScriptFile;

/**
 * Реализация менеджера ввода-вывода для файлов.
 * <p>
 * Читает команды из файла скрипта вместо консоли.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see IOManager
 */
public class FileIOManagerImpl implements IOManager{
    private FileInputStream fis;
    private InputStreamReader isr;
    private BufferedReader reader;
    private String path;

    public FileIOManagerImpl(String path){
        validateScriptFile(path);
        try {
            this.path = path;
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(Exceptions.FILE_NOT_FOUND);
        }
    }

    @Override
    public String read(){
        System.out.print(">> ");
        try {
            String data = reader.readLine();
            if(data == null){
                System.out.println();
                throw new RuntimeException(Exceptions.SCRIPT_END);
            }
            System.out.println(data);
            return data;
        }catch (IOException e){
            System.out.println();
        }
        throw new RuntimeException(Exceptions.SCRIPT_ERROR);
    }

    @Override
    public void write(String text) {
        System.out.println(text);
    }

    @Override
    public String askString(String text){
        write(text);
        return read();
    }

    @Override
    public Integer askInt(String text){
        write(text);
        String s = read();
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
    public Float askFloat(String text){
        write(text);
        String s = read().replace(',', '.');
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
        write(text);
        String s = read().replace(',', '.');
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
    public Long askLong(String text){
        write(text);
        String s = read();
        try {
            return Long.parseLong(read());
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

        if(isUpdateMode) write(String.valueOf(value));
        //TODO WATCH OTHER IMPL
        T newValue = input.get();

        if (validator.test(newValue)) {
            value = newValue;
            return value;
        }

         if(isUpdateMode && newValue == null) return value;


         throw new IllegalArgumentException("Invalid script input");
    }

    @Override
    public String getFile() {
        return path;
    }
}
