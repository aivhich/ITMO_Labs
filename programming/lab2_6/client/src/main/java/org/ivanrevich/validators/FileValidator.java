package org.ivanrevich.validators;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class FileValidator {

    public static void validateScriptFile(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException e) {
            throw new RuntimeException("Error: incorrect path -> " + path);
        }

        if (path.trim().isEmpty()) {
            throw new RuntimeException("Error: File path not specified");
        }

        File file = new File(path);

        if (!file.exists()) {
            throw new RuntimeException("Error: file does not exist -> " + path);
        }

        if (file.isDirectory()) {
            throw new RuntimeException("Error: The path specified is to a directory, not a file ->" + path);
        }


        if (!file.canRead()) {
            throw new RuntimeException("Error: File cannot be read ->" + path);
        }
    }

    public static void validateCollectionNewFile(String path, String expectableFormat, Boolean isRead) {
        try {
            Paths.get(path);
        } catch (InvalidPathException e) {
            throw new RuntimeException("Error: incorrect path -> " + path);
        }
        if (path.trim().isEmpty()) {
            throw new RuntimeException("Error: File path not specified");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            throw new RuntimeException("Error: The path specified is to a directory, not a file ->" + path);
        }

        if (expectableFormat!=null&&!path.toLowerCase().endsWith(expectableFormat)) {
            throw new RuntimeException("Error: File must have extension "+expectableFormat+" but " + path);
        }

        if(isRead){
            if(file.exists()) {
                if (!file.canRead()) {
                    throw new RuntimeException("Error: File cannot be read ->" + path + "\n Please add read permissions");
                }
            }
        }
        if (!file.canWrite() && !isRead && file.exists()) {
            throw new RuntimeException("Error: File cannot be write ->" + path+"\n Please add write permissions");
        }
    }
}