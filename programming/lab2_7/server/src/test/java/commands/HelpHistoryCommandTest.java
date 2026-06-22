package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Command;
import org.ivanrevich.commands.Help;
import org.ivanrevich.commands.History;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.managers.CommandManagerImpl;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server Help / History Command Tests")
class HelpHistoryCommandTest {

    private ManagersLocator locator;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(IOManager.class, new RecordingIOManager());
    }

    @Test
    @DisplayName("Help lists toString() of all registered commands (excluding Save)")
    void helpListsCommands() {
        CommandManagerImpl cm = new CommandManagerImpl(locator);
        locator.register(CommandManager.class, cm);

        cm.registerCommands(Map.of(
                "show", (Command) new Command() {
                    public Result<?> run(Request<?> r) { return null; }
                    public Result<?> run(String[] args) { return null; }
                    public String toString() { return "show: shows stuff"; }
                }
        ));

        Help help = new Help(locator);
        Result<String> result = help.run(new Request<>(CommandType.HELP, null));

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertTrue(result.getOutput().contains("show: shows stuff"));
        assertTrue(result.getOutput().contains("--- HELP ---"));
    }

    @Test
    @DisplayName("History returns only the last 13 entries by default")
    void historyReturnsLast13() {
        CommandManagerImpl cm = new CommandManagerImpl(locator);
        locator.register(CommandManager.class, cm);

        ArrayList<CommandObj> fakeHistory = cm.getHistory();
        for (int i = 0; i < 20; i++) {
            fakeHistory.add(new CommandObj("cmd" + i, new String[]{}));
        }

        History history = new History(locator);
        Result<List<CommandObj>> result = history.run(new Request<>(CommandType.HISTORY, null));

        assertEquals(13, result.getOutput().size());
        // last command in history should be cmd19
        assertEquals("cmd19", result.getOutput().get(result.getOutput().size() - 1).name());
    }

    @Test
    @DisplayName("History returns all entries when fewer than 13 exist")
    void historyReturnsAllWhenFewerThan13() {
        CommandManagerImpl cm = new CommandManagerImpl(locator);
        locator.register(CommandManager.class, cm);

        cm.getHistory().add(new CommandObj("only-one", new String[]{}));

        History history = new History(locator);
        Result<List<CommandObj>> result = history.run(new Request<>(CommandType.HISTORY, null));

        assertEquals(1, result.getOutput().size());
    }

    @Test
    @DisplayName("History run(String[]) with custom count respects argument")
    void historyConsoleWithCustomCount() {
        CommandManagerImpl cm = new CommandManagerImpl(locator);
        locator.register(CommandManager.class, cm);

        for (int i = 0; i < 10; i++) {
            cm.getHistory().add(new CommandObj("cmd" + i, new String[]{}));
        }

        History history = new History(locator);
        Result<?> result = history.run(new String[]{"3"});

        @SuppressWarnings("unchecked")
        List<CommandObj> output = (List<CommandObj>) result.getOutput();
        assertEquals(3, output.size());
    }
}
