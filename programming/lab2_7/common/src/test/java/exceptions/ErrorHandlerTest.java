package exceptions;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.exceptions.ErrorHandler;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorHandler Tests")
class ErrorHandlerTest {

    private final List<String> messages = new ArrayList<>();
    private boolean scriptEndCalled;
    private boolean scriptErrorCalled;
    private boolean commandCancelledCalled;
    private boolean commandSoftCancelledCalled;
    private boolean unauthorizedCalled;

    private ErrorHandler handler;

    @BeforeEach
    void setUp() {
        messages.clear();
        scriptEndCalled = false;
        scriptErrorCalled = false;
        commandCancelledCalled = false;
        commandSoftCancelledCalled = false;
        unauthorizedCalled = false;

        handler = new ErrorHandler(
                messages::add,
                () -> scriptEndCalled = true,
                () -> scriptErrorCalled = true,
                () -> commandCancelledCalled = true,
                () -> commandSoftCancelledCalled = true,
                () -> unauthorizedCalled = true
        ) {};
    }

    @Test
    @DisplayName("SCRIPT_END dispatches to onScriptEnd")
    void scriptEndDispatched() {
        handler.handle(new AppException(ResultCode.SCRIPT_END));
        assertTrue(scriptEndCalled);
    }

    @Test
    @DisplayName("SCRIPT_ERROR dispatches to onScriptError")
    void scriptErrorDispatched() {
        handler.handle(new AppException(ResultCode.SCRIPT_ERROR));
        assertTrue(scriptErrorCalled);
    }

    @Test
    @DisplayName("COMMAND_CANCELLED dispatches to onCommandCancelled")
    void commandCancelledDispatched() {
        handler.handle(new AppException(ResultCode.COMMAND_CANCELLED));
        assertTrue(commandCancelledCalled);
    }

    @Test
    @DisplayName("COMMAND_SOFT_CANCELLED dispatches to onCommandSoftCancelled")
    void commandSoftCancelledDispatched() {
        handler.handle(new AppException(ResultCode.COMMAND_SOFT_CANCELLED));
        assertTrue(commandSoftCancelledCalled);
    }

    @Test
    @DisplayName("UNAUTHORIZED_REQUEST dispatches to onUnauthorizedRequest")
    void unauthorizedDispatched() {
        handler.handle(new AppException(ResultCode.UNAUTHORIZED_REQUEST));
        assertTrue(unauthorizedCalled);
    }

    @Test
    @DisplayName("COMMAND_NOT_FOUND writes a message")
    void commandNotFoundWritesMessage() {
        handler.handle(new AppException(ResultCode.COMMAND_NOT_FOUND));
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).toLowerCase().contains("command not found"));
    }

    @Test
    @DisplayName("ID_ISN_EXIST writes a message")
    void idNotExistWritesMessage() {
        handler.handle(new AppException(ResultCode.ID_ISN_EXIST));
        assertEquals(1, messages.size());
    }

    @Test
    @DisplayName("FILE_NOT_FOUND writes a message")
    void fileNotFoundWritesMessage() {
        handler.handle(new AppException(ResultCode.FILE_NOT_FOUND));
        assertEquals(1, messages.size());
    }

    @Test
    @DisplayName("INVALID_NUM_OF_ARGS writes a message")
    void invalidNumOfArgsWritesMessage() {
        handler.handle(new AppException(ResultCode.INVALID_NUM_OF_ARGS));
        assertEquals(1, messages.size());
    }

    @Test
    @DisplayName("unhandled code falls back to writing the exception message")
    void defaultFallsBackToExceptionMessage() {
        handler.handle(new AppException(ResultCode.SUCCESS, "custom detail"));
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("custom detail"));
    }

    @Test
    @DisplayName("null onCommandSoftCancelled does not throw when handling that code")
    void nullSoftCancelledHandlerSafe() {
        ErrorHandler h = new ErrorHandler(
                messages::add,
                () -> {},
                () -> {},
                () -> {},
                null, // no soft-cancel handler
                () -> {}
        ) {};
        assertDoesNotThrow(() -> h.handle(new AppException(ResultCode.COMMAND_SOFT_CANCELLED)));
    }
}
