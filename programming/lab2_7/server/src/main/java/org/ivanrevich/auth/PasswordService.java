package org.ivanrevich.auth;

import org.ivanrevich.config.AppConfig;

import java.security.MessageDigest;

public class PasswordService {
    private static final String pepper = AppConfig.getSecret("pepper");

    public static byte[] getHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            return md.digest((password + pepper + salt).getBytes("UTF-8"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
