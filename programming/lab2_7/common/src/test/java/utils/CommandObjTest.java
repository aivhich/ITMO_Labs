package utils;

import org.ivanrevich.utils.CommandObj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CommandObj Tests")
class CommandObjTest {

    @Test
    @DisplayName("name accessor returns correct value")
    void nameAccessor() {
        CommandObj obj = new CommandObj("show", new String[]{});
        assertEquals("show", obj.name());
    }

    @Test
    @DisplayName("args accessor returns correct array")
    void argsAccessor() {
        String[] args = {"arg1", "arg2"};
        CommandObj obj = new CommandObj("update", args);
        assertArrayEquals(args, obj.args());
    }

    @Test
    @DisplayName("empty args array allowed")
    void emptyArgs() {
        CommandObj obj = new CommandObj("help", new String[]{});
        assertEquals(0, obj.args().length);
    }

    @Test
    @DisplayName("record equality: same name and args")
    void recordEquality() {
        String[] args = {"1"};
        CommandObj a = new CommandObj("remove_by_id", args);
        CommandObj b = new CommandObj("remove_by_id", args);
        // Records compare by value; arrays compare by reference in records
        // They share the same array reference here so equality holds
        assertEquals(a, b);
    }
}
