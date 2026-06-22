package managers;

import org.ivanrevich.managers.CommandManager;import org.ivanrevich.utils.CommandObj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the static CommandManager.parseCommand() method
 * shared by both client and server CommandManager interfaces.
 * We test it through the server version (both are identical).
 */
@DisplayName("CommandManager.parseCommand() Tests")
class CommandManagerParseCommandTest {

    // Using the server-side interface which has the static method
    @Test
    @DisplayName("single word command → name set, empty args")
    void singleWord() {
        CommandObj obj = CommandManager.parseCommand("show");
        assertEquals("show", obj.name());
        assertEquals(0, obj.args().length);
    }

    @Test
    @DisplayName("command with one arg → name and arg set")
    void commandWithOneArg() {
        CommandObj obj = CommandManager.parseCommand("remove_by_id 42");
        assertEquals("remove_by_id", obj.name());
        assertArrayEquals(new String[]{"42"}, obj.args());
    }

    @Test
    @DisplayName("command with multiple args")
    void commandWithMultipleArgs() {
        CommandObj obj = org.ivanrevich.managers.CommandManager.parseCommand("signup alice secret");
        assertEquals("signup", obj.name());
        assertArrayEquals(new String[]{"alice", "secret"}, obj.args());
    }

    @Test
    @DisplayName("command is lowercased")
    void commandIsLowercased() {
        CommandObj obj = org.ivanrevich.managers.CommandManager.parseCommand("SHOW");
        assertEquals("show", obj.name());
    }

    @Test
    @DisplayName("extra leading/trailing whitespace is trimmed")
    void whitespaceIsTrimmed() {
        CommandObj obj = org.ivanrevich.managers.CommandManager.parseCommand("  help  ");
        assertEquals("help", obj.name());
    }

    @Test
    @DisplayName("blank command does not throw but returns empty-name token")
    void blankCommandBehavior() {
        // split("  ") → [""] which has length 1 and name=""
        // The implementation does NOT throw on blank; it returns a CommandObj with name=""
        CommandObj obj = org.ivanrevich.managers.CommandManager.parseCommand("   ");
        // name is empty string (whitespace trimmed then split gives [""])
        assertNotNull(obj);
    }
}
