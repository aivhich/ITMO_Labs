package auth;

import org.ivanrevich.auth.PasswordService;
import org.ivanrevich.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@DisplayName("PasswordService Tests (static pepper mocked via Mockito)")
class PasswordServiceTest {

    @Test
    @DisplayName("getHash() produces a 48-byte SHA-384 digest")
    void getHashProducesShaThreeEightFourLength() {
        byte[] hash = PasswordService.getHash("myPassword", "somesalt");
        assertEquals(48, hash.length);
    }

    @Test
    @DisplayName("getHash() is deterministic for the same password/salt")
    void getHashIsDeterministic() {
        byte[] h1 = PasswordService.getHash("pw", "salt1");
        byte[] h2 = PasswordService.getHash("pw", "salt1");
        assertArrayEquals(h1, h2);
    }

    @Test
    @DisplayName("getHash() differs for different salts")
    void getHashDiffersWithDifferentSalt() {
        byte[] h1 = PasswordService.getHash("pw", "salt1");
        byte[] h2 = PasswordService.getHash("pw", "salt2");
        assertFalse(Arrays.equals(h1, h2));
    }

    @Test
    @DisplayName("getHash() differs for different passwords")
    void getHashDiffersWithDifferentPassword() {
        byte[] h1 = PasswordService.getHash("pw1", "salt");
        byte[] h2 = PasswordService.getHash("pw2", "salt");
        assertFalse(Arrays.equals(h1, h2));
    }

    @Test
    @DisplayName("AppConfig.getSecret() can be intercepted via mockStatic for isolated config tests")
    void appConfigGetSecretCanBeMockedStatically() {
        try (MockedStatic<AppConfig> mocked = mockStatic(AppConfig.class)) {
            mocked.when(() -> AppConfig.getSecret("pepper")).thenReturn("test-pepper-value");
            mocked.when(() -> AppConfig.getConfig("db.url", null)).thenReturn("jdbc:postgresql://test/db");

            assertEquals("test-pepper-value", AppConfig.getSecret("pepper"));
            assertEquals("jdbc:postgresql://test/db", AppConfig.getConfig("db.url", null));
        }
    }
}
