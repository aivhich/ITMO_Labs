package exceptions;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppException Tests")
class AppExceptionTest {

    @Test
    @DisplayName("constructor(ResultCode) sets code and message")
    void basicConstructor() {
        AppException ex = new AppException(ResultCode.COMMAND_NOT_FOUND);
        assertEquals(ResultCode.COMMAND_NOT_FOUND, ex.getCode());
        assertEquals(ResultCode.COMMAND_NOT_FOUND.getMessage(), ex.getMessage());
    }

    @Test
    @DisplayName("constructor(ResultCode, String) appends detail to message")
    void constructorWithDetail() {
        AppException ex = new AppException(ResultCode.INVALID_ARGS, "must be positive");
        assertEquals(ResultCode.INVALID_ARGS, ex.getCode());
        assertTrue(ex.getMessage().contains("must be positive"));
        assertTrue(ex.getMessage().contains(ResultCode.INVALID_ARGS.getMessage()));
    }

    @Test
    @DisplayName("constructor(ResultCode, Throwable) preserves cause")
    void constructorWithCause() {
        RuntimeException cause = new RuntimeException("original");
        AppException ex = new AppException(ResultCode.INTERNAL_CLI_ERROR, cause);
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, ex.getCode());
        assertSame(cause, ex.getCause());
    }

    @Test
    @DisplayName("AppException is a RuntimeException")
    void isRuntimeException() {
        assertInstanceOf(RuntimeException.class, new AppException(ResultCode.SUCCESS));
    }

    @Test
    @DisplayName("getCode() returns same enum as passed")
    void getCodeReturnsSameEnum() {
        for (ResultCode code : ResultCode.values()) {
            AppException ex = new AppException(code);
            assertSame(code, ex.getCode());
        }
    }
}
