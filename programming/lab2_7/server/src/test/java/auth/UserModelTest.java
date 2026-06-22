package auth;

import org.ivanrevich.auth.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
class UserModelTest {

    @Test
    @DisplayName("constructor with username/password sets username and generates salt+hash")
    void constructorSetsFields() {
        User user = new User("alice", "secret123");

        assertEquals("alice", user.getUsername());
        assertNotNull(user.getPassword());
        assertTrue(user.getPassword().length > 0);
    }

    @Test
    @DisplayName("isPasswordEqual() returns true for the original password")
    void isPasswordEqualTrueForCorrectPassword() {
        User user = new User("bob", "correct-password");
        assertTrue(user.isPasswordEqual("correct-password"));
    }

    @Test
    @DisplayName("isPasswordEqual() returns false for a wrong password")
    void isPasswordEqualFalseForWrongPassword() {
        User user = new User("bob", "correct-password");
        assertFalse(user.isPasswordEqual("wrong-password"));
    }

    @Test
    @DisplayName("updatePassword() changes the stored hash so the old password no longer matches")
    void updatePasswordInvalidatesOldPassword() {
        User user = new User("carol", "old-password");
        assertTrue(user.isPasswordEqual("old-password"));

        user.updatePassword("new-password");

        assertFalse(user.isPasswordEqual("old-password"));
        assertTrue(user.isPasswordEqual("new-password"));
    }

    @Test
    @DisplayName("setId/getId round-trip")
    void idRoundTrip() {
        User user = new User();
        user.setId(42);
        assertEquals(42, user.getId());
    }

    @Test
    @DisplayName("setUsername/getUsername round-trip")
    void usernameRoundTrip() {
        User user = new User();
        user.setUsername("dave");
        assertEquals("dave", user.getUsername());
    }

    @Test
    @DisplayName("two users with the same password have different hashes (different salts)")
    void differentUsersDifferentHashesEvenWithSamePassword() {
        User u1 = new User("user1", "samepassword");
        User u2 = new User("user2", "samepassword");

        assertFalse(java.util.Arrays.equals(u1.getPassword(), u2.getPassword()));
    }
}
