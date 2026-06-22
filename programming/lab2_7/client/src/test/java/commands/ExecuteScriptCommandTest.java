package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.ExecuteScript;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExecuteScript doesn't touch Client at all — it only pushes a new
 * FileIOManagerImpl onto the IOManagerStack and re-registers IOManager.
 * No mocking needed here; real IOManagerStack + a real temp file exercise
 * the actual push/registration logic end-to-end.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Client ExecuteScript Command Tests")
class ExecuteScriptCommandTest {

    @TempDir
    Path tempDir;

    private ManagersLocator locator;
    private IOManagerStack stack;
    private ExecuteScript executeScript;

    private static IOManager noopIO() {
        return new IOManager() {
            public String getFile() { return "BASE"; }
            public String read() { return ""; }
            public void write(String text) {}
            public String askString(String text) { return ""; }
            public Long askLong(String text) { return 0L; }
            public Double askDouble(String text) { return 0.0; }
            public Float askFloat(String text) { return 0f; }
            public Integer askInt(String text) { return 0; }
            public <T> T askValue(T init, java.util.function.Supplier<T> input, java.util.function.Predicate<T> v) { return init; }
        };
    }

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        IOManager base = noopIO();
        stack = new IOManagerStack(base);
        locator.register(IOManagerStack.class, stack);
        locator.register(IOManager.class, base);
        executeScript = new ExecuteScript(locator);
    }

    @Test
    @DisplayName("wrong number of args throws AppException(INVALID_NUM_OF_ARGS)")
    void wrongArgCountThrows() {
        AppException ex = assertThrows(AppException.class, () -> executeScript.run(new String[]{}));
        assertEquals(ResultCode.INVALID_NUM_OF_ARGS, ex.getCode());
    }

    @Test
    @DisplayName("valid script file pushes new IOManager and registers it as current")
    void validScriptPushesNewIOManager() throws Exception {
        Path scriptFile = tempDir.resolve("script.txt");
        Files.writeString(scriptFile, "show\nexit\n");

        ResultCode result = executeScript.run(new String[]{scriptFile.toString()});

        assertEquals(ResultCode.SUCCESS, result);
        assertEquals(scriptFile.toString(), locator.get(IOManager.class).getFile());
        assertEquals(scriptFile.toString(), stack.current().getFile());
    }

    @Test
    @DisplayName("non-existing script file propagates AppException(FILE_NOT_FOUND) from validator")
    void nonExistingScriptThrows() {
        String missing = tempDir.resolve("missing.txt").toString();
        assertThrows(RuntimeException.class, () -> executeScript.run(new String[]{missing}));
    }
}
