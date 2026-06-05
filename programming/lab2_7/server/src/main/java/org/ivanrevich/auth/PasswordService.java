package org.ivanrevich.auth;

import org.ivanrevich.config.AppConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordService {
    private static final String pepper = AppConfig.getSecret("pepper");

    public static byte[] getHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            return md.digest((password + pepper + salt).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
