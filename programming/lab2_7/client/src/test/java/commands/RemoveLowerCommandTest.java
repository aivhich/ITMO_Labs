package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.RemoveLower;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client RemoveLower Command Tests")
class RemoveLowerCommandTest {

    @Mock
    Client client;
    @Mock
    AuthManager authManager;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    RemoveLower removeLower;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(AuthManager.class, authManager);
        locator.register(IOManager.class, ioManager);
        removeLower = new RemoveLower(locator);

        lenient().when(ioManager.askValue(any(), any(), any())).thenAnswer(invocation -> {
            java.util.function.Supplier<?> input = invocation.getArgument(1);
            java.util.function.Predicate validator = invocation.getArgument(2);
            Object value = input.get();
            return validator.test(value) ? value : invocation.getArgument(0);
        });
    }

    @Test
    @DisplayName("sends reference vehicle with authorId and reports removed count")
    void sendsReferenceVehicleAndReportsCount() throws Exception {
        when(ioManager.askFloat(anyString())).thenReturn(100f);
        when(ioManager.askLong(anyString())).thenReturn(4L);
        when(authManager.authorizedUserId()).thenReturn(9);

        Response r = new Response<>(ResultCode.SUCCESS, "ok", 3);
        when(client.sendObject(any())).thenReturn(r);

        ResultCode result = removeLower.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write(contains("Removed 3"));

        var captor = org.mockito.ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.REMOVE_LOWER, captor.getValue().getCommandType());
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(ioManager.askFloat(anyString())).thenReturn(100f);
        when(ioManager.askLong(anyString())).thenReturn(4L);
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        assertEquals(ResultCode.INTERNAL_CLI_ERROR, removeLower.run(new String[]{}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(ioManager.askFloat(anyString())).thenReturn(100f);
        when(ioManager.askLong(anyString())).thenReturn(4L);
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());

        assertEquals(ResultCode.INVALID_REQUEST, removeLower.run(new String[]{}));
    }
}
