package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.RemoveById;
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

@DisplayName("Server RemoveById Command Tests")
class RemoveByIdCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private StubUserManager userManager;
    private RemoveById removeById;

    private Credentials ownerCreds;
    private Credentials otherCreds;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        userManager = new StubUserManager();

        locator.register(QueueManager.class, queueManager);
        locator.register(UserManager.class, userManager);

        removeById = new RemoveById(locator);

        int ownerId = userManager.registerUser("owner");
        userManager.registerUser("other");

        ownerCreds = new Credentials("owner", "correct-password");
        otherCreds = new Credentials("other", "correct-password");
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
    @DisplayName("owner can remove their own vehicle")
    void ownerCanRemoveOwnVehicle() {
        int ownerId = userManager.getIdForUser(ownerCreds);
        Vehicle v = vehicleOwnedBy(ownerId);
        queueManager.add(v);

        Request<Integer> req = new Request<>(CommandType.REMOVE_BY_ID, v.getId());
        req.setCredentials(ownerCreds);

        Result<?> result = removeById.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("non-owner cannot remove someone else's vehicle")
    void nonOwnerCannotRemove() {
        int ownerId = userManager.getIdForUser(ownerCreds);
        Vehicle v = vehicleOwnedBy(ownerId);
        queueManager.add(v);

        Request<Integer> req = new Request<>(CommandType.REMOVE_BY_ID, v.getId());
        req.setCredentials(otherCreds);

        Result<?> result = removeById.run(req);

        assertEquals(ResultCode.HAVENT_OWNER_RULES, result.getResultCode());
        assertEquals(1, queueManager.size()); // still present
    }

    @Test
    @DisplayName("removing non-existing id returns ID_ISN_EXIST")
    void removingNonExistingIdFails() {
        Request<Integer> req = new Request<>(CommandType.REMOVE_BY_ID, 99999);
        req.setCredentials(ownerCreds);

        Result<?> result = removeById.run(req);

        assertEquals(ResultCode.ID_ISN_EXIST, result.getResultCode());
    }

    @Test
    @DisplayName("run(String[]) variant removes by id directly from collection")
    void runWithArgsRemovesDirectly() {
        Vehicle v = vehicleOwnedBy(1);
        queueManager.add(v);

        Result<?> result = removeById.run(new String[]{String.valueOf(v.getId())});

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("run(String[]) with wrong number of args throws AppException")
    void runWithWrongArgsThrows() {
        assertThrows(org.ivanrevich.exceptions.AppException.class,
                () -> removeById.run(new String[]{}));
    }
}
