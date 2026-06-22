package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Info;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Info Command Tests")
class InfoCommandTest {

    @Mock
    Client client;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    Info info;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(IOManager.class, ioManager);
        info = new Info(locator);
    }

    @Test
    @DisplayName("writes type, count and init date from the server response map")
    void writesAllInfoFields() throws Exception {
        Map<String, String> body = Map.of(
                "type", "PriorityQueue",
                "count", "5",
                "init_date", "2026-01-01T00:00:00Z"
        );
        Response r =new Response<>(ResultCode.SUCCESS, "ok", body);
        when(client.sendObject(any())).thenReturn(r);

        ResultCode result = info.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write("Collection type: PriorityQueue");
        verify(ioManager).write("Number of elements: 5");
        verify(ioManager).write("Init date: 2026-01-01T00:00:00Z");
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, info.run(new String[]{}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, info.run(new String[]{}));
    }
}
