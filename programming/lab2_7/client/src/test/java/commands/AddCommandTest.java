package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Add;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Add Command Tests")
class AddCommandTest {

    @Mock
    Client client;
    @Mock
    AuthManager authManager;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    Add addCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(AuthManager.class, authManager);
        locator.register(IOManager.class, ioManager);
        addCommand = new Add(locator);

        lenient().when(ioManager.askValue(any(), any(), any())).thenAnswer(invocation -> {
            java.util.function.Supplier<?> input = invocation.getArgument(1);
            java.util.function.Predicate validator = invocation.getArgument(2);
            Object value = input.get();
            return validator.test(value) ? value : invocation.getArgument(0);
        });
    }

    private void scriptValidVehicleInput() {
        when(ioManager.askString(anyString())).thenReturn("Falcon");
        when(ioManager.askFloat(anyString())).thenReturn(100f);
        when(ioManager.askLong(anyString())).thenReturn(4L);
        when(ioManager.askInt(anyString())).thenReturn(1); // selects first enum option each time
        when(ioManager.askDouble(anyString())).thenReturn(0.0);
    }

    @Test
    @DisplayName("sends ADD request with vehicle built from user input and authorId from AuthManager")
    void sendsCorrectAddRequest() throws Exception {
        scriptValidVehicleInput();
        when(authManager.authorizedUserId()).thenReturn(7);
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.SUCCESS, "ok", null));

        ResultCode result = addCommand.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Request<Vehicle>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());

        Request<Vehicle> sent = captor.getValue();
        assertEquals(CommandType.ADD, sent.getCommandType());
        assertEquals("Falcon", sent.getArgs().getName());
        assertEquals(7, sent.getArgs().getAuthorId());
    }

    @Test
    @DisplayName("propagates server ResultCode back to caller")
    void propagatesServerResultCode() throws Exception {
        scriptValidVehicleInput();
        when(authManager.authorizedUserId()).thenReturn(1);
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.INVALID_INPUT, "bad", null));

        ResultCode result = addCommand.run(new String[]{});

        assertEquals(ResultCode.INVALID_INPUT, result);
    }

    @Test
    @DisplayName("IOException from Client.sendObject maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        scriptValidVehicleInput();
        when(authManager.authorizedUserId()).thenReturn(1);
        when(client.sendObject(any())).thenThrow(new java.io.IOException("network down"));

        ResultCode result = addCommand.run(new String[]{});

        assertEquals(ResultCode.INTERNAL_CLI_ERROR, result);
    }

    @Test
    @DisplayName("ClassNotFoundException from Client.sendObject maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        scriptValidVehicleInput();
        when(authManager.authorizedUserId()).thenReturn(1);
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException("bad payload"));

        ResultCode result = addCommand.run(new String[]{});

        assertEquals(ResultCode.INVALID_REQUEST, result);
    }
}
