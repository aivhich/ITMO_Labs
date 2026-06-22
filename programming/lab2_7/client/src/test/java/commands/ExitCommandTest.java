package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Exit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exit.run() calls System.exit(0) directly, which terminates the JVM running
 * the test process. SecurityManager-based interception is deprecated/removed
 * as of Java 17+ (JEP 411), so there is no safe, portable way to unit-test
 * the exit call itself without forking a subprocess.
 * <p>
 * We only verify the parts that don't require invoking run(): the help text.
 * If you need to verify the exit behaviour itself, run Exit.run() in a
 * forked JVM process (e.g. via ProcessBuilder) and assert on its exit code.
 */
@DisplayName("Client Exit Command Tests")
class ExitCommandTest {

    @Test
    @DisplayName("toString() returns expected help text")
    void toStringHelpText() {
        Exit exit = new Exit(new ManagersLocator());
        assertEquals("exit: terminate the program", exit.toString());
    }
}
