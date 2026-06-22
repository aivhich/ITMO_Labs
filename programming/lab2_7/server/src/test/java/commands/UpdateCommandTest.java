package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.Update;
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

@DisplayName("Server Update Command Tests")
class UpdateCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private StubUserManager userManager;
    private Update updateCommand;
    private int ownerId;
    private Credentials ownerCreds;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        userManager = new StubUserManager();
        locator.register(QueueManager.class, queueManager);
        locator.register(UserManager.class, userManager);
        updateCommand = new Update(locator);

        ownerId = userManager.registerUser("owner");
        ownerCreds = new Credentials("owner", "correct-password");
    }

    private Vehicle vehicle(int authorId, String name) {
        Vehicle v = new Vehicle();
        v.setName(name);
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
    @DisplayName("owner can update their own vehicle with valid data")
    void ownerCanUpdate() {
        Vehicle original = vehicle(ownerId, "Original");
        queueManager.add(original);

        Vehicle replacement = vehicle(ownerId, "Updated");
        replacement.setId(original.getId());

        Request<Vehicle> req = new Request<>(CommandType.UPDATE, replacement);
        req.setCredentials(ownerCreds);

        Result<?> result = updateCommand.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals("Updated", queueManager.getById(original.getId()).getName());
    }

    @Test
    @DisplayName("updating with invalid data fails validation")
    void updateWithInvalidDataFails() {
        Vehicle original = vehicle(ownerId, "Original");
        queueManager.add(original);

        Vehicle replacement = vehicle(ownerId, "");
        replacement.setId(original.getId());

        Request<Vehicle> req = new Request<>(CommandType.UPDATE, replacement);
        req.setCredentials(ownerCreds);

        Result<?> result = updateCommand.run(req);

        assertEquals(ResultCode.INVALID_INPUT, result.getResultCode());
    }

    @Test
    @DisplayName("updating non-existing id returns ID_ISN_EXIST")
    void updateNonExistingId() {
        Vehicle replacement = vehicle(ownerId, "Ghost");
        replacement.setId(999);

        Request<Vehicle> req = new Request<>(CommandType.UPDATE, replacement);
        req.setCredentials(ownerCreds);

        Result<?> result = updateCommand.run(req);

        assertEquals(ResultCode.ID_ISN_EXIST, result.getResultCode());
    }

    @Test
    @DisplayName("non-owner cannot update someone else's vehicle")
    void nonOwnerCannotUpdate() {
        int otherId = userManager.registerUser("intruder");
        Credentials intruderCreds = new Credentials("intruder", "correct-password");

        Vehicle original = vehicle(ownerId, "Original");
        queueManager.add(original);

        Vehicle replacement = vehicle(otherId, "Hacked");
        replacement.setId(original.getId());

        Request<Vehicle> req = new Request<>(CommandType.UPDATE, replacement);
        req.setCredentials(intruderCreds);

        Result<?> result = updateCommand.run(req);

        assertEquals(ResultCode.HAVENT_OWNER_RULES, result.getResultCode());
    }
}
