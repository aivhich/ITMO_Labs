package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.RemoveHead;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client RemoveHead Command Tests")
class RemoveHeadCommandTest {

    @Mock
    Client client;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    RemoveHead removeHead;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(IOManager.class, ioManager);
        removeHead = new RemoveHead(locator);
    }

    private Vehicle vehicle() {
        Vehicle v = new Vehicle();
        v.setName("Head");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    @Test
    @DisplayName("non-null body writes the removed vehicle")
    void writesRemovedVehicle() throws Exception {
        Vehicle v = vehicle();
        Response r = new Response<>(ResultCode.SUCCESS, "ok", v);
        when(client.sendObject(any())).thenReturn(r);

        ResultCode result = removeHead.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write(v.toString());
    }

    @Test
    @DisplayName("null body writes the 'no elements' notice")
    void nullBodyWritesNotice() throws Exception {
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.SUCCESS, "ok", null));

        removeHead.run(new String[]{});

        verify(ioManager).write("No elements in the collection");
    }

    @Test
    @DisplayName("request sent has REMOVE_HEAD command type")
    void sendsCorrectCommandType() throws Exception {
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.SUCCESS, "ok", null));

        removeHead.run(new String[]{});

        var captor = org.mockito.ArgumentCaptor.forClass(org.ivanrevich.requests.Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.REMOVE_HEAD, captor.getValue().getCommandType());
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, removeHead.run(new String[]{}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, removeHead.run(new String[]{}));
    }
}
