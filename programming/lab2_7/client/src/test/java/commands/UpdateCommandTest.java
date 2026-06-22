package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Update;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.managers.CommandManager;
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
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Update Command Tests")
class UpdateCommandTest {

    @Mock
    Client client;
    @Mock
    AuthManager authManager;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    Update updateCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(AuthManager.class, authManager);
        locator.register(IOManager.class, ioManager);
        updateCommand = new Update(locator);

        lenient().when(ioManager.askValue(any(), any(), any())).thenAnswer(invocation -> {
            java.util.function.Supplier<?> input = invocation.getArgument(1);
            java.util.function.Predicate validator = invocation.getArgument(2);
            Object value = input.get();
            return validator.test(value) ? value : invocation.getArgument(0);
        });
    }

    private Vehicle existingVehicle(int id) {
        Vehicle v = new Vehicle();
        v.setId(id);
        v.setName("Original");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    private void scriptKeepAllOldValues() {
        lenient().when(ioManager.askString(anyString())).thenReturn("");
        lenient().when(ioManager.askFloat(anyString())).thenReturn(null);
        lenient().when(ioManager.askLong(anyString())).thenReturn(null);
        lenient().when(ioManager.askInt(anyString())).thenReturn(null);
        lenient().when(ioManager.askDouble(anyString())).thenReturn(null);
    }

    @Test
    @DisplayName("wrong number of args returns INVALID_NUM_OF_ARGS without contacting the server")
    void wrongArgCount() {
        ResultCode result = updateCommand.run(new String[]{});
        assertEquals(ResultCode.INVALID_NUM_OF_ARGS, result);
        verifyNoInteractions(client);
    }

    @Test
    @DisplayName("non-numeric id returns INVALID_ARGS without contacting the server")
    void nonNumericId() {
        ResultCode result = updateCommand.run(new String[]{"abc"});
        assertEquals(ResultCode.INVALID_ARGS, result);
        verifyNoInteractions(client);
    }

    @Test
    @DisplayName("id not present in fetched collection returns ID_ISN_EXIST")
    void idNotInCollection() throws Exception {
        PriorityQueue<Vehicle> queue = new PriorityQueue<>();
        queue.add(existingVehicle(1));

        Response r2 = new Response<>(ResultCode.SUCCESS, "ok", queue);

        when(client.sendObject(argThat(r -> r.getCommandType() == CommandType.SHOW)))
                .thenReturn(r2);

        ResultCode result = updateCommand.run(new String[]{"999"});

        assertEquals(ResultCode.ID_ISN_EXIST, result);
        verify(client, times(1)).sendObject(any()); // only SHOW, never UPDATE
    }

    @Test
    @DisplayName("found id: fetches via SHOW, sends UPDATE with new authorId, propagates result")
    void successfulUpdateFlow() throws Exception {
        PriorityQueue<Vehicle> queue = new PriorityQueue<>();
        queue.add(existingVehicle(5));
        scriptKeepAllOldValues();

        Response r2 = new Response<>(ResultCode.SUCCESS, "ok", queue);
        when(client.sendObject(argThat(r -> r != null && r.getCommandType() == CommandType.SHOW)))
                .thenReturn(r2);
        when(client.sendObject(argThat(r -> r != null && r.getCommandType() == CommandType.UPDATE)))
                .thenReturn(new Response<>(ResultCode.SUCCESS, "updated", null));

        when(authManager.authorizedUserId()).thenReturn(3);

        ResultCode result = updateCommand.run(new String[]{"5"});

        assertEquals(ResultCode.SUCCESS, result);

        ArgumentCaptor<Request<Vehicle>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(argThat(r -> r != null && r.getCommandType() == CommandType.UPDATE));
        verify(client, times(2)).sendObject(any());
    }

    @Test
    @DisplayName("IOException during SHOW maps to INTERNAL_CLI_ERROR")
    void ioExceptionDuringShow() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, updateCommand.run(new String[]{"1"}));
    }

    @Test
    @DisplayName("ClassNotFoundException during SHOW maps to INVALID_REQUEST")
    void classNotFoundDuringShow() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, updateCommand.run(new String[]{"1"}));
    }
}
