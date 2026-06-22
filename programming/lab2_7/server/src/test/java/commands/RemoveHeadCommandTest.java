package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.commands.RemoveHead;
import org.ivanrevich.managers.IOManager;
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

@DisplayName("Server RemoveHead Command Tests")
class RemoveHeadCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private StubUserManager userManager;
    private RemoveHead removeHead;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        userManager = new StubUserManager();
        locator.register(QueueManager.class, queueManager);
        locator.register(UserManager.class, userManager);
        locator.register(IOManager.class, new RecordingIOManager());
        removeHead = new RemoveHead(locator);
    }

    private Vehicle vehicle(int authorId, float power) {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(power);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        v.setAuthorId(authorId);
        return v;
    }

    @Test
    @DisplayName("on empty collection, run(Request) returns SUCCESS with null body")
    void emptyCollectionReturnsNullBody() {
        Request<Void> req = new Request<>(CommandType.REMOVE_HEAD, null);
        req.setCredentials(new Credentials("anyone", "x"));

        Result<?> result = removeHead.run(req);

        assertEquals(ResultCode.SUCCESS, result.getResultCode());
        assertNull(result.getOutput());
    }

    @Test
    @DisplayName("run(String[]) removes and returns head element")
    void consoleVariantRemovesHead() {
        int id = userManager.registerUser("admin");
        Vehicle v = vehicle(id, 50f);
        queueManager.add(v);

        Result<?> result = removeHead.run(new String[]{});

        assertEquals(0, queueManager.size());
        assertEquals(ResultCode.SUCCESS, result.getResultCode());
    }
}
