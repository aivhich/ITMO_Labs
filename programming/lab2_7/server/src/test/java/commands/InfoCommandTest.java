package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Info;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Server Info Command Tests")
class InfoCommandTest {

    private ManagersLocator locator;
    private QueueManager queueManager;
    private Info info;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        queueManager = new QueueManagerImpl();
        locator.register(QueueManager.class, queueManager);
        locator.register(IOManager.class, new RecordingIOManager());
        info = new Info(locator);
    }

    private Vehicle vehicle() {
        Vehicle v = new Vehicle();
        v.setName("V");
        v.setEnginePower(100f);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    @Test
    @DisplayName("Info on empty collection reports count 0")
    void infoEmptyCollection() {
        Result<?> result = info.run(new Request<>(CommandType.INFO, null));

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) result.getOutput();
        assertEquals("0", map.get("count"));
    }

    @Test
    @DisplayName("Info reports correct count after additions")
    void infoReportsCorrectCount() {
        queueManager.add(vehicle());
        queueManager.add(vehicle());

        Result<?> result = info.run(new Request<>(CommandType.INFO, null));

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) result.getOutput();
        assertEquals("2", map.get("count"));
        assertNotNull(map.get("type"));
        assertNotNull(map.get("init_date"));
    }

    @Test
    @DisplayName("Info result code is always SUCCESS")
    void infoAlwaysSuccess() {
        Result<?> result = info.run(new Request<>(CommandType.INFO, null));
        assertEquals(ResultCode.SUCCESS, result.getResultCode());
    }
}
