package utils;

import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.Deserializer;
import org.ivanrevich.utils.RequestDeserializer;
import org.ivanrevich.utils.Serializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Serializer / Deserializer round-trip Tests")
class SerializerDeserializerTest {

    private final Serializer serializer = new Serializer();

    private Vehicle sampleVehicle() {
        Vehicle v = new Vehicle();
        v.setId(7);
        v.setName("Speeder");
        v.setEnginePower(500f);
        v.setNumberOfWheels(2L);
        v.setCoordinates(new Coordinates(1.5, 2.5f));
        v.setCreationDate(new Date(0));
        v.setType(VehicleType.MOTORCYCLE);
        v.setFuelType(FuelType.PLASMA);
        v.setAuthorId(42);
        return v;
    }

    @Test
    @DisplayName("serialize then deserialize a String preserves value")
    void stringRoundTrip() throws IOException, ClassNotFoundException {
        String original = "hello world";
        ByteBuffer buf = serializer.serialize(original);
        String result = new Deserializer<String>().deserialize(buf.array());
        assertEquals(original, result);
    }

    @Test
    @DisplayName("serialize then deserialize a Vehicle preserves all fields")
    void vehicleRoundTrip() throws IOException, ClassNotFoundException {
        Vehicle original = sampleVehicle();
        ByteBuffer buf = serializer.serialize(original);
        Vehicle result = new Deserializer<Vehicle>().deserialize(buf.array());

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getEnginePower(), result.getEnginePower());
        assertEquals(original.getNumberOfWheels(), result.getNumberOfWheels());
        assertEquals(original.getType(), result.getType());
        assertEquals(original.getFuelType(), result.getFuelType());
        assertEquals(original.getAuthorId(), result.getAuthorId());
    }

    @Test
    @DisplayName("serialize then deserialize a Request preserves commandType and args")
    void requestRoundTrip() throws IOException, ClassNotFoundException {
        Vehicle v = sampleVehicle();
        Request<Vehicle> req = new Request<>(CommandType.ADD, v);

        ByteBuffer buf = serializer.serialize(req);
        Request<?> result = RequestDeserializer.deserialize(buf.array());

        assertEquals(CommandType.ADD, result.getCommandType());
        Vehicle resultVehicle = (Vehicle) result.getArgs();
        assertEquals(v.getName(), resultVehicle.getName());
    }

    @Test
    @DisplayName("serialized bytes are non-empty")
    void serializedNonEmpty() throws IOException {
        ByteBuffer buf = serializer.serialize("test");
        assertTrue(buf.limit() > 0);
    }
}
