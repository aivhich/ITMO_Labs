package org.ivanrevich.auth;

import java.security.SecureRandom;
import java.util.HexFormat;

public class SaltGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }
}