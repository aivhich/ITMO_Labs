package org.ivanrevich.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties config = load("application.properties");
    private static final Properties secrets = load("secrets.properties");

    private static Properties load(String filename) {
        Properties p = new Properties();
        try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream(filename)) {
            if (is != null) p.load(is);
            else System.out.println(filename + " not found, using env vars");
        } catch (IOException e) {
            System.out.println("Failed to load " + filename);
        }
        return p;
    }

    public static String getConfig(String key, String fallback) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envVal = System.getenv(envKey);

        if (envVal != null) return envVal;
        return config.getProperty(key, fallback);
    }

    public static String getSecret(String key) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envVal = System.getenv(envKey);
        if (envVal != null) return envVal;

        String val = secrets.getProperty(key);
        if (val == null || val.isBlank()) throw new RuntimeException("Secret '" + key + "' is not set");
        return val;
    }
}