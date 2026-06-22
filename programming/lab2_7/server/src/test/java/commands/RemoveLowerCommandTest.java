package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.RemoveLower;
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

@DisplayName("Server RemoveLower Command Tests")
class RemoveLowerCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private StubUserManager userManager;
    private RemoveLower removeLower;
    private int ownerId;
    private Credentials ownerCreds;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        userManager = new StubUserManager();
        locator.register(QueueManager.class, queueManager);
        locator.register(UserManager.class, userManager);
        removeLower = new RemoveLower(locator);

        ownerId = userManager.registerUser("owner");
        ownerCreds = new Credentials("owner", "correct-password");
    }

    private Vehicle vehicle(int authorId, float power, long wheels) {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(power);
        v.setNumberOfWheels(wheels);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        v.setAuthorId(authorId);
        return v;
    }

    @Test
    @DisplayName("removes only owned vehicles lower than reference")
    void removesOnlyOwnedLowerVehicles() {
        queueManager.add(vehicle(ownerId, 50f, 4));   // lower, owned -> removed
        queueManager.add(vehicle(ownerId, 200f, 4));  // higher, owned -> kept
        int otherId = userManager.registerUser("other");
        queueManager.add(vehicle(otherId, 10f, 2));   // lower, NOT owned -> kept

        Vehicle reference = vehicle(ownerId, 100f, 4);
        Request<Vehicle> req = new Request<>(CommandType.REMOVE_LOWER, reference);
        req.setCredentials(ownerCreds);

        Result<?> result = removeLower.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(1, (int) result.getOutput());
        assertEquals(2, queueManager.size());
    }

    @Test
    @DisplayName("no elements lower than reference → removes nothing")
    void noElementsLowerRemovesNothing() {
        queueManager.add(vehicle(ownerId, 500f, 8));

        Vehicle reference = vehicle(ownerId, 10f, 1);
        Request<Vehicle> req = new Request<>(CommandType.REMOVE_LOWER, reference);
        req.setCredentials(ownerCreds);

        Result<?> result = removeLower.run(req);

        assertEquals(0, (int) result.getOutput());
        assertEquals(1, queueManager.size());
    }

    @Test
    @DisplayName("null reference returns INVALID_INPUT")
    void nullReferenceFails() {
        Request<Vehicle> req = new Request<>(CommandType.REMOVE_LOWER, null);
        req.setCredentials(ownerCreds);

        Result<?> result = removeLower.run(req);

        assertEquals(ResultCode.INVALID_INPUT, result.getResultCode());
    }
}
