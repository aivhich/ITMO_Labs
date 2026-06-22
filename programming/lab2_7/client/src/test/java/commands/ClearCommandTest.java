package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Clear;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Clear Command Tests")
class ClearCommandTest {

    @Mock
    Client client;

    ManagersLocator locator;
    Clear clearCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        clearCommand = new Clear(locator);
    }

    @Test
    @DisplayName("sends CLEAR request and returns server ResultCode")
    void sendsClearRequest() throws Exception {
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.SUCCESS, "ok", null));

        ResultCode result = clearCommand.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        var captor = org.mockito.ArgumentCaptor.forClass(org.ivanrevich.requests.Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.CLEAR, captor.getValue().getCommandType());
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, clearCommand.run(new String[]{}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, clearCommand.run(new String[]{}));
    }
}
