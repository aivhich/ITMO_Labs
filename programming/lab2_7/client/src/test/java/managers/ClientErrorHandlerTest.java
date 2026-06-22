package managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.ClientErrorHandler;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client ClientErrorHandler Tests")
class ClientErrorHandlerTest {

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

            @Override
            public void close() {

            }
        };
    }

    private ManagersLocator locator;
    private List<String> writtenLines;
    private AtomicBoolean workMode;
    private IOManagerStack stack;
    private IOManager baseIO;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        writtenLines = new ArrayList<>();
        workMode = new AtomicBoolean(true);
        baseIO = recordingIO("CONSOLE", writtenLines);
        stack = new IOManagerStack(baseIO);
        locator.register(IOManagerStack.class, stack);
        locator.register(IOManager.class, baseIO);
    }

    @Test
    @DisplayName("COMMAND_CANCELLED sets workMode flag to false")
    void commandCancelledStopsWorkMode() {
        ClientErrorHandler handler = new ClientErrorHandler(locator, baseIO, workMode);

        handler.handle(new AppException(ResultCode.COMMAND_CANCELLED));

        assertFalse(workMode.get());
    }

    @Test
    @DisplayName("SCRIPT_END pops the IOManagerStack and re-registers previous IOManager")
    void scriptEndPopsStack() {
        List<String> scriptLines = new ArrayList<>();
        IOManager scriptIO = recordingIO("/path/script.txt", scriptLines);
        stack.push(scriptIO);
        locator.register(IOManager.class, scriptIO);

        ClientErrorHandler handler = new ClientErrorHandler(locator, scriptIO, workMode);
        handler.handle(new AppException(ResultCode.SCRIPT_END));

        assertSame(baseIO, stack.current());
        assertSame(baseIO, locator.get(IOManager.class));
    }

    @Test
    @DisplayName("SCRIPT_ERROR pops the stack and writes an error notice")
    void scriptErrorPopsStackAndWritesNotice() {
        List<String> scriptLines = new ArrayList<>();
        IOManager scriptIO = recordingIO("/path/bad_script.txt", scriptLines);
        stack.push(scriptIO);
        locator.register(IOManager.class, scriptIO);

        ClientErrorHandler handler = new ClientErrorHandler(locator, scriptIO, workMode);
        handler.handle(new AppException(ResultCode.SCRIPT_ERROR));

        assertSame(baseIO, stack.current());
        assertTrue(scriptLines.contains("Script execution error."));
    }

    @Test
    @DisplayName("UNAUTHORIZED_REQUEST writes an auth-required notice")
    void unauthorizedRequestWritesNotice() {
        ClientErrorHandler handler = new ClientErrorHandler(locator, baseIO, workMode);

        handler.handle(new AppException(ResultCode.UNAUTHORIZED_REQUEST));

        assertTrue(writtenLines.stream().anyMatch(l -> l.contains("not authorized")));
    }

    @Test
    @DisplayName("COMMAND_NOT_FOUND writes the default 'command not found' message")
    void commandNotFoundWritesMessage() {
        ClientErrorHandler handler = new ClientErrorHandler(locator, baseIO, workMode);

        handler.handle(new AppException(ResultCode.COMMAND_NOT_FOUND));

        assertTrue(writtenLines.stream().anyMatch(l -> l.toLowerCase().contains("command not found")));
    }
}
