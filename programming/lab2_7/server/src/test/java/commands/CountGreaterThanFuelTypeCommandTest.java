package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.CountGreaterThanFuelType;
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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server CountGreaterThanFuelType Command Tests")
class CountGreaterThanFuelTypeCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private CountGreaterThanFuelType command;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        locator.register(QueueManager.class, queueManager);
        command = new CountGreaterThanFuelType(locator);
    }

    private Vehicle vehicleWithFuel(FuelType fuel) {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(fuel);
        return v;
    }

    @Test
    @DisplayName("counts vehicles with fuelType ordinal greater than given")
    void countsCorrectly() {
        queueManager.add(vehicleWithFuel(FuelType.KEROSENE));   // 0
        queueManager.add(vehicleWithFuel(FuelType.MANPOWER));   // 1
        queueManager.add(vehicleWithFuel(FuelType.NUCLEAR));    // 2
        queueManager.add(vehicleWithFuel(FuelType.PLASMA));     // 3
        queueManager.add(vehicleWithFuel(FuelType.ANTIMATTER)); // 4

        Request<FuelType> req = new Request<>(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, FuelType.MANPOWER);
        Result<?> result = command.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(3L, result.getOutput()); // NUCLEAR, PLASMA, ANTIMATTER
    }

    @Test
    @DisplayName("no vehicles greater than the highest fuel type → count 0")
    void noGreaterVehicles() {
        queueManager.add(vehicleWithFuel(FuelType.KEROSENE));

        Request<FuelType> req = new Request<>(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, FuelType.ANTIMATTER);
        Result<?> result = command.run(req);

        assertEquals(0L, result.getOutput());
    }

    @Test
    @DisplayName("empty collection → count 0")
    void emptyCollection() {
        Request<FuelType> req = new Request<>(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, FuelType.KEROSENE);
        Result<?> result = command.run(req);

        assertEquals(0L, result.getOutput());
    }
}
