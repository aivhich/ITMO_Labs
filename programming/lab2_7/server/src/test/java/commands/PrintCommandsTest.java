package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.PrintAscending;
import org.ivanrevich.commands.PrintUniqueFuelType;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.QueueManagerImpl;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server PrintUniqueFuelType / PrintAscending Command Tests")
class PrintCommandsTest {

    private ManagersLocator locator;
    private QueueManager queueManager;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        locator.register(QueueManager.class, queueManager);
        locator.register(IOManager.class, new RecordingIOManager());
    }

    private Vehicle vehicle(float power, FuelType fuel) {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(power);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(fuel);
        return v;
    }

    @Test
    @DisplayName("PrintUniqueFuelType returns distinct fuel types")
    void printUniqueFuelTypeReturnsDistinct() {
        queueManager.add(vehicle(100f, FuelType.KEROSENE));
        queueManager.add(vehicle(200f, FuelType.KEROSENE));
        queueManager.add(vehicle(300f, FuelType.PLASMA));

        PrintUniqueFuelType cmd = new PrintUniqueFuelType(locator);
        Request<Void> req = new Request<>(CommandType.PRINT_UNIQUE_FUEL_TYPE, null);

        Result<?> result = cmd.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        @SuppressWarnings("unchecked")
        Set<FuelType> output = (Set<FuelType>) result.getOutput();
        assertEquals(2, output.size());
        assertTrue(output.contains(FuelType.KEROSENE));
        assertTrue(output.contains(FuelType.PLASMA));
    }

    @Test
    @DisplayName("PrintUniqueFuelType on empty collection returns empty set")
    void printUniqueFuelTypeEmptyCollection() {
        PrintUniqueFuelType cmd = new PrintUniqueFuelType(locator);
        Result<?> result = cmd.run(new Request<>(CommandType.PRINT_UNIQUE_FUEL_TYPE, null));

        @SuppressWarnings("unchecked")
        Set<FuelType> output = (Set<FuelType>) result.getOutput();
        assertTrue(output.isEmpty());
    }

    @Test
    @DisplayName("PrintAscending returns vehicles sorted by natural order")
    void printAscendingSortsCorrectly() {
        queueManager.add(vehicle(300f, FuelType.KEROSENE));
        queueManager.add(vehicle(50f, FuelType.KEROSENE));
        queueManager.add(vehicle(150f, FuelType.KEROSENE));

        PrintAscending cmd = new PrintAscending(locator);
        Result<?> result = cmd.run(new Request<>(CommandType.PRINT_ASCENDING, null));

        @SuppressWarnings("unchecked")
        List<Vehicle> sorted = (List<Vehicle>) result.getOutput();
        assertEquals(3, sorted.size());
        assertEquals(50f, sorted.get(0).getEnginePower());
        assertEquals(150f, sorted.get(1).getEnginePower());
        assertEquals(300f, sorted.get(2).getEnginePower());
    }

    @Test
    @DisplayName("PrintAscending on empty collection returns empty list")
    void printAscendingEmptyCollection() {
        PrintAscending cmd = new PrintAscending(locator);
        Result<?> result = cmd.run(new Request<>(CommandType.PRINT_ASCENDING, null));

        @SuppressWarnings("unchecked")
        List<Vehicle> sorted = (List<Vehicle>) result.getOutput();
        assertTrue(sorted.isEmpty());
    }
}
