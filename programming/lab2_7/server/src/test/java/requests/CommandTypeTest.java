package requests;

import org.ivanrevich.requests.CommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CommandType Tests")
class CommandTypeTest {

    @Test
    @DisplayName("getName() returns correct name for ADD")
    void addName() {
        assertEquals("add", CommandType.ADD.getName());
    }

    @Test
    @DisplayName("getName() returns correct name for REMOVE_BY_ID")
    void removeByIdName() {
        assertEquals("remove_by_id", CommandType.REMOVE_BY_ID.getName());
    }

    @Test
    @DisplayName("toString() returns the same as getName()")
    void toStringEqualsGetName() {
        for (CommandType ct : CommandType.values()) {
            assertEquals(ct.getName(), ct.toString(), "Mismatch for: " + ct);
        }
    }

    @Test
    @DisplayName("all command types have non-blank names")
    void allHaveNonBlankNames() {
        for (CommandType ct : CommandType.values()) {
            assertNotNull(ct.getName());
            assertFalse(ct.getName().isBlank(), "Blank name for: " + ct);
        }
    }

    @Test
    @DisplayName("LOGIN name is 'login'")
    void loginName() {
        assertEquals("login", CommandType.LOGIN.getName());
    }

    @Test
    @DisplayName("SIGNUP name is 'signup'")
    void signupName() {
        assertEquals("signup", CommandType.SIGNUP.getName());
    }
}
