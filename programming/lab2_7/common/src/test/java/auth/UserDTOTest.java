package auth;

import org.ivanrevich.auth.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserDTO Tests")
class UserDTOTest {

    @Test
    @DisplayName("record accessors return constructor values")
    void accessors() {
        UserDTO dto = new UserDTO(5, "alice");
        assertEquals(5, dto.id());
        assertEquals("alice", dto.username());
    }

    @Test
    @DisplayName("two DTOs with same values are equal")
    void equality() {
        UserDTO a = new UserDTO(1, "bob");
        UserDTO b = new UserDTO(1, "bob");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("DTOs with different ids are not equal")
    void inequality() {
        UserDTO a = new UserDTO(1, "bob");
        UserDTO b = new UserDTO(2, "bob");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("UserDTO is Serializable")
    void isSerializable() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(UserDTO.class));
    }
}
