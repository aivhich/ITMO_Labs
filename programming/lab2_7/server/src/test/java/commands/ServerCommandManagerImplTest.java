package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.Command;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.CommandManagerImpl;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server CommandManagerImpl Tests")
class ServerCommandManagerImplTest {

    private ManagersLocator locator;
    private StubUserManager userManager;
    private CommandManagerImpl commandManager;

    /** Dummy command that records whether it ran and what it returns. */
    static class DummyCommand implements Command {
        boolean ran = false;
        ResultCode codeToReturn = ResultCode.SUCCESS;

        @Override
        public Result<?> run(Request<?> r) {
            ran = true;
            return new Result<>(codeToReturn, "dummy", "dummy-output");
        }

        @Override
        public Result<?> run(String[] args) {
            ran = true;
            return new Result<>(codeToReturn, "dummy", "dummy-output");
        }
    }

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        userManager = new StubUserManager();
        locator.register(UserManager.class, userManager);
        locator.register(IOManager.class, new RecordingIOManager());
        commandManager = new CommandManagerImpl(locator);
    }

    @Test
    @DisplayName("unauthorized request (no credentials) to protected command is blocked")
    void unauthorizedRequestBlocked() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of(CommandType.SHOW.getName(), dummy));

        Request<Void> req = new Request<>(CommandType.SHOW, null);
        // no credentials set

        Result<?> result = commandManager.run(req);

        assertEquals(ResultCode.UNAUTHORIZED_REQUEST, result.getResultCode());
        assertFalse(dummy.ran);
    }

    @Test
    @DisplayName("unauthorized request (invalid credentials) to protected command is blocked")
    void invalidCredentialsBlocked() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of(CommandType.SHOW.getName(), dummy));

        Request<Void> req = new Request<>(CommandType.SHOW, null);
        req.setCredentials(new Credentials("ghost", "wrong"));

        Result<?> result = commandManager.run(req);

        assertEquals(ResultCode.UNAUTHORIZED_REQUEST, result.getResultCode());
        assertFalse(dummy.ran);
    }

    @Test
    @DisplayName("authorized request to protected command runs and is recorded in history")
    void authorizedRequestRuns() {
        userManager.registerUser("alice");
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of(CommandType.SHOW.getName(), dummy));

        Request<Void> req = new Request<>(CommandType.SHOW, null);
        req.setCredentials(new Credentials("alice", "correct-password"));

        Result<?> result = commandManager.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertTrue(dummy.ran);
        assertEquals(1, commandManager.getHistory().size());
        assertEquals("show", commandManager.getHistory().get(0).name());
    }

    @Test
    @DisplayName("public command runs without any credentials")
    void publicCommandRunsWithoutCredentials() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerNoAuthCommands(Map.of(CommandType.LOGIN.getName(), dummy));

        Request<Void> req = new Request<>(CommandType.LOGIN, null);
        // no credentials needed for public commands

        Result<?> result = commandManager.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertTrue(dummy.ran);
    }

    @Test
    @DisplayName("unknown command name throws AppException COMMAND_NOT_FOUND")
    void unknownCommandThrows() {
        userManager.registerUser("alice");
        Request<Void> req = new Request<>(CommandType.HELP, null); // not registered
        req.setCredentials(new Credentials("alice", "correct-password"));

        assertThrows(AppException.class, () -> commandManager.run(req));
    }

    @Test
    @DisplayName("run(String) executes a registered console command and records history")
    void consoleCommandExecutesAndRecordsHistory() {
        DummyCommand dummy = new DummyCommand();
        commandManager.registerCommands(Map.of("greet", dummy));

        commandManager.run("greet world");

        assertTrue(dummy.ran);
        assertEquals(1, commandManager.getHistory().size());
        assertEquals("greet", commandManager.getHistory().get(0).name());
    }

    @Test
    @DisplayName("run(String) with unregistered command throws AppException")
    void consoleUnknownCommandThrows() {
        assertThrows(AppException.class, () -> commandManager.run("doesnotexist"));
    }

    @Test
    @DisplayName("getRegistedCommands returns all registered protected commands")
    void getRegisteredCommands() {
        DummyCommand d1 = new DummyCommand();
        DummyCommand d2 = new DummyCommand();
        commandManager.registerCommands(Map.of("cmd1", d1, "cmd2", d2));

        assertEquals(2, commandManager.getRegistedCommands().size());
    }

    @Test
    @DisplayName("getPubCommands returns all registered public commands")
    void getPubCommands() {
        DummyCommand d1 = new DummyCommand();
        commandManager.registerNoAuthCommands(Map.of("login", d1));

        assertEquals(1, commandManager.getPubCommands().size());
    }
}
