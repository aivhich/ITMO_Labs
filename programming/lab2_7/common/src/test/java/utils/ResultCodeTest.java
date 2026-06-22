package utils;

import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResultCode Tests")
class ResultCodeTest {

    @Test
    @DisplayName("SUCCESS has correct message")
    void successMessage() {
        assertEquals("Success", ResultCode.SUCCESS.getMessage());
    }

    @Test
    @DisplayName("toString returns the message")
    void toStringReturnsMessage() {
        assertEquals(ResultCode.SUCCESS.getMessage(), ResultCode.SUCCESS.toString());
    }

    @Test
    @DisplayName("fromMessage finds SUCCESS by message")
    void fromMessageSuccess() {
        assertEquals(ResultCode.SUCCESS, ResultCode.fromMessage("Success"));
    }

    @Test
    @DisplayName("fromMessage finds COMMAND_NOT_FOUND")
    void fromMessageCommandNotFound() {
        assertEquals(ResultCode.COMMAND_NOT_FOUND, ResultCode.fromMessage("Command not found"));
    }

    @Test
    @DisplayName("fromMessage throws for unknown message")
    void fromMessageUnknown() {
        assertThrows(IllegalArgumentException.class,
                () -> ResultCode.fromMessage("this message does not exist"));
    }

    @Test
    @DisplayName("UNAUTHORIZED_REQUEST has non-null message")
    void unauthorizedHasMessage() {
        assertNotNull(ResultCode.UNAUTHORIZED_REQUEST.getMessage());
        assertFalse(ResultCode.UNAUTHORIZED_REQUEST.getMessage().isBlank());
    }

    @Test
    @DisplayName("all enum values have non-blank messages")
    void allCodesHaveMessages() {
        for (ResultCode code : ResultCode.values()) {
            assertNotNull(code.getMessage(), "Null message for: " + code);
            assertFalse(code.getMessage().isBlank(), "Blank message for: " + code);
        }
    }
}
