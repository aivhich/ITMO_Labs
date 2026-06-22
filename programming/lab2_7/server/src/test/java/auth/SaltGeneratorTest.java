package auth;

import org.ivanrevich.auth.SaltGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SaltGenerator Tests")
class SaltGeneratorTest {

    @Test
    @DisplayName("generateSalt() returns a 32-character hex string (16 bytes)")
    void saltLength() {
        String salt = SaltGenerator.generateSalt();
        assertEquals(32, salt.length());
        assertTrue(salt.matches("[0-9a-f]+"));
    }

    @RepeatedTest(5)
    @DisplayName("repeated calls produce different salts")
    void saltsAreRandom() {
        String s1 = SaltGenerator.generateSalt();
        String s2 = SaltGenerator.generateSalt();
        assertNotEquals(s1, s2);
    }
}
