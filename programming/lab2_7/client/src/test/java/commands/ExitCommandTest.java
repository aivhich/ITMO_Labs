package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Exit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Client Exit Command Tests")
class ExitCommandTest {

    @Test
    @DisplayName("toString() returns expected help text")
    void toStringHelpText() {
        Exit exit = new Exit(new ManagersLocator());
        assertEquals("exit: terminate the program", exit.toString());
    }
}
