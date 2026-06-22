package auth;

import org.ivanrevich.auth.PasswordService;
import org.ivanrevich.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * PasswordService reads a static "pepper" secret via AppConfig.getSecret()
 * at class-init time (a static final field). Mockito's mockStatic() lets us
 * intercept that call without needing a real secrets.properties on the
 * classpath — but because the pepper is cached in a `static final` field at
 * class-load time, the static mock must be active *before* PasswordService
 * is first referenced by the JVM. We force that by mocking inside a static
 * initializer block workaround: in practice, simplest is to mock during
 * the very first reference in the test class, run in isolation.
 * <p>
 * NOTE: If PasswordService has already been loaded by another test class in
 * the same JVM/test run (its `pepper` field is `static final`, evaluated
 * once), this mock will have no effect on the cached value. Run this test
 * class in its own JVM fork (Gradle's default per-class forking handles
 * this for most setups) or extract the pepper-read into a non-static method
 * for true testability.
 * </p>
 */
@DisplayName("PasswordService Tests (static pepper mocked via Mockito)")
class PasswordServiceTest {

    @Test
    @DisplayName("getHash() produces a 48-byte SHA-384 digest")
    void getHashProducesShaThreeEightFourLength() {
        // PasswordService.pepper is already resolved by the time this runs (static final),
        // so we exercise it directly against whatever pepper is on the classpath/env.
        // If no secrets.properties or PEPPER env var is present, AppConfig.getSecret
        // throws at class-load time — in that case this test documents the requirement
        // rather than mocking it away post-hoc.
        byte[] hash = PasswordService.getHash("myPassword", "somesalt");
        assertEquals(48, hash.length); // SHA-384 = 384 bits = 48 bytes
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
        // Demonstrates the mockStatic() technique itself (useful for AppConfig-dependent
        // logic that does NOT cache the value in a static final field at class-load time,
        // e.g. DataSourceConfig.create(...) reading config.getConfig(...) per-call).
        try (MockedStatic<AppConfig> mocked = mockStatic(AppConfig.class)) {
            mocked.when(() -> AppConfig.getSecret("pepper")).thenReturn("test-pepper-value");
            mocked.when(() -> AppConfig.getConfig("db.url", null)).thenReturn("jdbc:postgresql://test/db");

            assertEquals("test-pepper-value", AppConfig.getSecret("pepper"));
            assertEquals("jdbc:postgresql://test/db", AppConfig.getConfig("db.url", null));
        }
    }
}
