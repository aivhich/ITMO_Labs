package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Help;
import org.ivanrevich.commands.History;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Help / History Command Tests")
class HelpHistoryCommandTest {

    @Mock
    Client client;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(IOManager.class, ioManager);
    }

    @Test
    @DisplayName("Help writes the server-provided help text")
    void helpWritesServerText() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", "--- HELP ---\nadd: ...");
        when(client.sendObject(any())).thenReturn(r);

        Help help = new Help(locator);
        ResultCode result = help.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write("--- HELP ---\nadd: ...");
    }

    @Test
    @DisplayName("Help IOException maps to INTERNAL_CLI_ERROR")
    void helpIoException() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        Help help = new Help(locator);
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, help.run(new String[]{}));
    }

    @Test
    @DisplayName("History writes the name of each returned command")
    void historyWritesEachCommandName() throws Exception {
        ArrayList<CommandObj> history = new ArrayList<>();
        history.add(new CommandObj("show", new String[]{}));
        history.add(new CommandObj("add", new String[]{}));

        Response r = new Response<>(ResultCode.SUCCESS, "ok", history);
        when(client.sendObject(any())).thenReturn(r);

        History historyCmd = new History(locator);
        ResultCode result = historyCmd.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write("show");
        verify(ioManager).write("add");
    }

    @Test
    @DisplayName("History ClassNotFoundException maps to INVALID_REQUEST")
    void historyClassNotFound() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());

        History historyCmd = new History(locator);
        assertEquals(ResultCode.INVALID_REQUEST, historyCmd.run(new String[]{}));
    }
}
