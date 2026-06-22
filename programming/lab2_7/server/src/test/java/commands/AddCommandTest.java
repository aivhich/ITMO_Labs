package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Add;
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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server Add Command Tests")
class AddCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private Add addCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        locator.register(QueueManager.class, queueManager);
        locator.register(IOManager.class, new RecordingIOManager());
        addCommand = new Add(locator);
    }

    private Vehicle validVehicle() {
        Vehicle v = new Vehicle();
        v.setName("TestCar");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    @Test
    @DisplayName("run(Request) with valid vehicle succeeds and adds to queue")
    void addValidVehicleSucceeds() {
        Request<Vehicle> req = new Request<>(CommandType.ADD, validVehicle());

        Result<?> result = addCommand.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(1, queueManager.size());
    }

    @Test
    @DisplayName("run(Request) with invalid vehicle (empty name) fails validation")
    void addInvalidVehicleFails() {
        Vehicle v = validVehicle();
        v.setName("");
        Request<Vehicle> req = new Request<>(CommandType.ADD, v);

        Result<?> result = addCommand.run(req);

        assertEquals(ResultCode.INVALID_INPUT, result.getResultCode());
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("run(Request) with invalid enginePower (0) fails validation")
    void addInvalidEnginePowerFails() {
        Vehicle v = validVehicle();
        v.setEnginePower(0f);
        Request<Vehicle> req = new Request<>(CommandType.ADD, v);

        Result<?> result = addCommand.run(req);

        assertEquals(ResultCode.INVALID_INPUT, result.getResultCode());
    }

    @Test
    @DisplayName("run(Request) assigns generated id to the vehicle")
    void addAssignsId() {
        Vehicle v = validVehicle();
        Request<Vehicle> req = new Request<>(CommandType.ADD, v);

        addCommand.run(req);

        assertTrue(v.getId() > 0);
    }

    @Test
    @DisplayName("multiple Add calls increase the queue size accordingly")
    void multipleAddsIncreaseSize() {
        addCommand.run(new Request<>(CommandType.ADD, validVehicle()));
        addCommand.run(new Request<>(CommandType.ADD, validVehicle()));
        addCommand.run(new Request<>(CommandType.ADD, validVehicle()));

        assertEquals(3, queueManager.size());
    }

    @Test
    @DisplayName("toString() returns expected help text")
    void toStringHelpText() {
        assertTrue(addCommand.toString().startsWith("add"));
    }
}
