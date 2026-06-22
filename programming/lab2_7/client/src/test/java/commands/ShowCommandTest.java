package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Show;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Show Command Tests")
class ShowCommandTest {

    @Mock
    Client client;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    Show showCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(IOManager.class, ioManager);
        showCommand = new Show(locator);
    }

    private Vehicle vehicle(String name) {
        Vehicle v = new Vehicle();
        v.setName(name);
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    @Test
    @DisplayName("requests SHOW and writes each vehicle to IOManager")
    void writesEachVehicle() throws Exception {
        PriorityQueue<Vehicle> queue = new PriorityQueue<>();
        queue.add(vehicle("A"));
        queue.add(vehicle("B"));

        Response r = new Response<>(ResultCode.SUCCESS, "ok", queue);
        when(client.sendObject(any())).thenReturn(r);

        ResultCode result = showCommand.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager, times(2)).write(anyString());

        ArgumentCaptor<Request<?>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.SHOW, captor.getValue().getCommandType());
    }

    @Test
    @DisplayName("empty collection writes the 'empty' notice")
    void emptyCollectionWritesNotice() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", new PriorityQueue<Vehicle>());
        when(client.sendObject(any())).thenReturn(r);

        showCommand.run(new String[]{});

        verify(ioManager).write("Priority queue is empty");
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        ResultCode result = showCommand.run(new String[]{});

        assertEquals(ResultCode.INTERNAL_CLI_ERROR, result);
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());

        ResultCode result = showCommand.run(new String[]{});

        assertEquals(ResultCode.INVALID_REQUEST, result);
    }
}
