package managers;

import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.CommandManagerImpl;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client CommandManagerImpl Tests")
class ClientCommandManagerImplTest {

    /** Dummy command (client-side signature: ResultCode run(String[])). */
    static class DummyCommand implements Command {
        boolean ran = false;
        String[] receivedArgs;
        ResultCode codeToReturn = ResultCode.SUCCESS;

        @Override
        public ResultCode run(String[] args) {
            ran = true;
            receivedArgs = args;
            return codeToReturn;
        }
    }

    private CommandManagerImpl commandManager;

    @BeforeEach
    void setUp() {
        commandManager = new CommandManagerImpl();
    }

    @Test
    @DisplayName("registered protected command runs with parsed args")
    void protectedCommandRunsWithArgs() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of("add", dummy));

        commandManager.run("add arg1 arg2");

        assertTrue(dummy.ran);
        assertArrayEquals(new String[]{"arg1", "arg2"}, dummy.receivedArgs);
    }

    @Test
    @DisplayName("registered public (no-auth) command runs without restriction")
    void publicCommandRuns() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerNoAuthCommands(Map.of("login", dummy));

        commandManager.run("login alice secret");

        assertTrue(dummy.ran);
        assertArrayEquals(new String[]{"alice", "secret"}, dummy.receivedArgs);
    }

    @Test
    @DisplayName("public commands take priority over protected commands with the same name")
    void publicCommandTakesPriority() {
        DummyCommand pub = new DummyCommand();
        DummyCommand protectedCmd = new DummyCommand();
        commandManager.registerNoAuthCommands(Map.of("shared", pub));
        commandManager.registerCommands(Map.of("shared", protectedCmd));

        commandManager.run("shared");

        assertTrue(pub.ran);
        assertFalse(protectedCmd.ran);
    }

    @Test
    @DisplayName("unregistered command throws AppException(COMMAND_NOT_FOUND)")
    void unknownCommandThrows() {
        AppException ex = assertThrows(AppException.class, () -> commandManager.run("doesnotexist"));
        assertEquals(ResultCode.COMMAND_NOT_FOUND, ex.getCode());
    }

    @Test
    @DisplayName("getRegistedCommands returns all registered protected commands")
    void getRegisteredCommands() {
        commandManager.registerCommands(Map.of("a", new DummyCommand(), "b", new DummyCommand()));
        assertEquals(2, commandManager.getRegistedCommands().size());
    }

    @Test
    @DisplayName("getPubCommands returns all registered public commands")
    void getPubCommands() {
        commandManager.registerNoAuthCommands(Map.of("login", new DummyCommand()));
        assertEquals(1, commandManager.getPubCommands().size());
    }

    @Test
    @DisplayName("command name is case-insensitive (lowercased by parser)")
    void commandNameCaseInsensitive() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of("show", dummy));

        commandManager.run("SHOW");

        assertTrue(dummy.ran);
    }
}
