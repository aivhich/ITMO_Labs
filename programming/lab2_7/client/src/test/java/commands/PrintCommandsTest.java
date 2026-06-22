package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.PrintAscending;
import org.ivanrevich.commands.PrintUniqueFuelType;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.network.Client;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client PrintAscending / PrintUniqueFuelType Command Tests")
class PrintCommandsTest {

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

    private Vehicle vehicle(String name, FuelType fuel) {
        Vehicle v = new Vehicle();
        v.setName(name);
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(fuel);
        return v;
    }

    @Test
    @DisplayName("PrintAscending writes each returned vehicle in order")
    void printAscendingWritesEachVehicle() throws Exception {
        List<Vehicle> list = List.of(vehicle("A", FuelType.KEROSENE), vehicle("B", FuelType.PLASMA));
        Response r = new Response<>(ResultCode.SUCCESS, "ok", list);
        when(client.sendObject(any())).thenReturn(r);

        PrintAscending cmd = new PrintAscending(locator);
        ResultCode result = cmd.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager, times(2)).write(anyString());
    }

    @Test
    @DisplayName("PrintAscending IOException maps to INTERNAL_CLI_ERROR")
    void printAscendingIoException() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        PrintAscending cmd = new PrintAscending(locator);
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, cmd.run(new String[]{}));
    }

    @Test
    @DisplayName("PrintUniqueFuelType writes header + each unique fuel type")
    void printUniqueFuelTypeWritesEntries() throws Exception {
        Set<FuelType> uniques = Set.of(FuelType.KEROSENE, FuelType.PLASMA);
        Response r = new Response<>(ResultCode.SUCCESS, "ok", uniques);
        when(client.sendObject(any())).thenReturn(r);

        PrintUniqueFuelType cmd = new PrintUniqueFuelType(locator);
        ResultCode result = cmd.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        // header line + one line per unique type (2)
        verify(ioManager, times(3)).write(anyString());
    }

    @Test
    @DisplayName("PrintUniqueFuelType on empty set writes the empty-collection notice")
    void printUniqueFuelTypeEmptySet() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", Set.of());
        when(client.sendObject(any())).thenReturn(r);

        PrintUniqueFuelType cmd = new PrintUniqueFuelType(locator);
        cmd.run(new String[]{});

        verify(ioManager).write("Collection is empty, no fuel types available.");
    }

    @Test
    @DisplayName("PrintUniqueFuelType ClassNotFoundException maps to INVALID_REQUEST")
    void printUniqueFuelTypeClassNotFound() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());

        PrintUniqueFuelType cmd = new PrintUniqueFuelType(locator);
        assertEquals(ResultCode.INVALID_REQUEST, cmd.run(new String[]{}));
    }
}
