package auth;

import org.ivanrevich.auth.Credentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Credentials Tests")
class CredentialsTest {

    @Test
    @DisplayName("constructor sets username and password")
    void constructor() {
        Credentials c = new Credentials("alice", "secret");
        assertEquals("alice", c.getUsername());
        assertEquals("secret", c.getPassword());
    }

    @Test
    @DisplayName("setUsername updates value")
    void setUsername() {
        Credentials c = new Credentials("old", "pass");
        c.setUsername("new");
        assertEquals("new", c.getUsername());
    }

    @Test
    @DisplayName("setPassword updates value")
    void setPassword() {
        Credentials c = new Credentials("user", "old");
        c.setPassword("newpass");
        assertEquals("newpass", c.getPassword());
    }

    @Test
    @DisplayName("Credentials is Serializable")
    void isSerializable() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(Credentials.class));
    }
}
