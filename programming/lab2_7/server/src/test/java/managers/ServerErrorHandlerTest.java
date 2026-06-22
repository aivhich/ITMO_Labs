package managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.managers.ServerErrorHandler;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server ServerErrorHandler Tests")
class ServerErrorHandlerTest {

    private static IOManager recordingIO(String file, List<String> sink) {
        return new IOManager() {
            public String getFile() { return file; }
            public String read() { return ""; }
            public void write(String text) { sink.add(text); }
            public String askString(String text) { return ""; }
            public Long askLong(String text) { return 0L; }
            public Double askDouble(String text) { return 0.0; }
            public Float askFloat(String text) { return 0f; }
            public Integer askInt(String text) { return 0; }
            public <T> T askValue(T init, java.util.function.Supplier<T> input, java.util.function.Predicate<T> v) { return init; }
        };
    }

    private ManagersLocator locator;
    private List<String> writtenLines;
    private IOManagerStack stack;
    private IOManager baseIO;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        writtenLines = new ArrayList<>();
        baseIO = recordingIO("CONSOLE", writtenLines);
        stack = new IOManagerStack(baseIO);
        locator.register(IOManagerStack.class, stack);
        locator.register(IOManager.class, baseIO);
    }

    @Test
    @DisplayName("SCRIPT_END pops the stack and restores the previous IOManager")
    void scriptEndPopsStack() {
        List<String> scriptLines = new ArrayList<>();
        IOManager scriptIO = recordingIO("/path/script.txt", scriptLines);
        stack.push(scriptIO);
        locator.register(IOManager.class, scriptIO);

        ServerErrorHandler handler = new ServerErrorHandler(locator, scriptIO, false);
        handler.handle(new AppException(ResultCode.SCRIPT_END));

        assertSame(baseIO, stack.current());
        assertSame(baseIO, locator.get(IOManager.class));
    }

    @Test
    @DisplayName("SCRIPT_ERROR pops the stack and writes an error notice")
    void scriptErrorPopsStackAndWritesNotice() {
        List<String> scriptLines = new ArrayList<>();
        IOManager scriptIO = recordingIO("/path/bad.txt", scriptLines);
        stack.push(scriptIO);
        locator.register(IOManager.class, scriptIO);

        ServerErrorHandler handler = new ServerErrorHandler(locator, scriptIO, false);
        handler.handle(new AppException(ResultCode.SCRIPT_ERROR));

        assertSame(baseIO, stack.current());
        assertTrue(scriptLines.contains("Script execution error."));
    }

    @Test
    @DisplayName("COMMAND_CANCELLED in raw mode writes a 'Command cancelled' notice")
    void commandCancelledRawModeWritesNotice() {
        ServerErrorHandler handler = new ServerErrorHandler(locator, baseIO, true);
        handler.handle(new AppException(ResultCode.COMMAND_CANCELLED));

        assertTrue(writtenLines.contains("Command cancelled"));
    }

    @Test
    @DisplayName("COMMAND_CANCELLED outside raw mode writes nothing")
    void commandCancelledNonRawModeWritesNothing() {
        ServerErrorHandler handler = new ServerErrorHandler(locator, baseIO, false);
        handler.handle(new AppException(ResultCode.COMMAND_CANCELLED));

        assertTrue(writtenLines.isEmpty());
    }

    @Test
    @DisplayName("COMMAND_SOFT_CANCELLED in raw mode writes a soft-cancel notice")
    void commandSoftCancelledRawModeWritesNotice() {
        ServerErrorHandler handler = new ServerErrorHandler(locator, baseIO, true);
        handler.handle(new AppException(ResultCode.COMMAND_SOFT_CANCELLED));

        assertTrue(writtenLines.contains("Command cancelled (soft)"));
    }

    @Test
    @DisplayName("COMMAND_NOT_FOUND writes the default message")
    void commandNotFoundWritesDefaultMessage() {
        ServerErrorHandler handler = new ServerErrorHandler(locator, baseIO, false);
        handler.handle(new AppException(ResultCode.COMMAND_NOT_FOUND));

        assertTrue(writtenLines.stream().anyMatch(l -> l.toLowerCase().contains("command not found")));
    }
}
