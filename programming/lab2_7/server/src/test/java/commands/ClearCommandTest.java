package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.Clear;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.QueueManagerImpl;
import org.ivanrevich.managers.UserManager;
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

@DisplayName("Server Clear Command Tests")
class ClearCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private StubUserManager userManager;
    private Clear clearCommand;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        userManager = new StubUserManager();
        locator.register(QueueManager.class, queueManager);
        locator.register(UserManager.class, userManager);
        clearCommand = new Clear(locator);
    }

    private Vehicle vehicleOwnedBy(int authorId) {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        v.setAuthorId(authorId);
        return v;
    }

    @Test
    @DisplayName("run(Request) clears only vehicles owned by the requesting user")
    void clearOnlyOwnVehicles() {
        int user1Id = userManager.registerUser("u1");
        int user2Id = userManager.registerUser("u2");
        queueManager.add(vehicleOwnedBy(user1Id));
        queueManager.add(vehicleOwnedBy(user2Id));

        Request<Void> req = new Request<>(CommandType.CLEAR, null);
        req.setCredentials(new Credentials("u1", "correct-password"));

        Result<?> result = clearCommand.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(1, queueManager.size());
        assertEquals(user2Id, queueManager.getAll().peek().getAuthorId());
    }

    @Test
    @DisplayName("run(String[]) clears the entire collection")
    void clearAllViaConsole() {
        queueManager.add(vehicleOwnedBy(1));
        queueManager.add(vehicleOwnedBy(2));

        Result<?> result = clearCommand.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("clearing an already-empty collection still succeeds")
    void clearEmptyCollectionSucceeds() {
        Result<?> result = clearCommand.run(new String[]{});
        assertEquals(ResultCode.SUCCESS, result.getResultCode());
    }
}
