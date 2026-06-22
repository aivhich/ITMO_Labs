package utils;

import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.utils.GenericBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GenericBuilder Tests")
class GenericBuilderTest {

    @Test
    @DisplayName("builds an object with a single field")
    void singleField() {
        Coordinates c = GenericBuilder.of(Coordinates::new)
                .with(Coordinates::setX, 5.0)
                .build();
        assertEquals(5.0, c.getX());
        assertNull(c.getY());
    }

    @Test
    @DisplayName("builds an object with multiple fields")
    void multipleFields() {
        Coordinates c = GenericBuilder.of(Coordinates::new)
                .with(Coordinates::setX, 10.0)
                .with(Coordinates::setY, 20f)
                .build();
        assertEquals(10.0, c.getX());
        assertEquals(20f, c.getY());
    }

    @Test
    @DisplayName("builds a Vehicle with all fields via chained .with()")
    void vehicleBuilder() {
        Date d = new Date(0);
        Vehicle v = GenericBuilder.of(Vehicle::new)
                .with(Vehicle::setId, 1)
                .with(Vehicle::setName, "TestCar")
                .with(Vehicle::setEnginePower, 200f)
                .with(Vehicle::setNumberOfWheels, 4L)
                .with(Vehicle::setCoordinates, new Coordinates(1.0, 1f))
                .with(Vehicle::setCreationDate, d)
                .with(Vehicle::setType, VehicleType.MOTORCYCLE)
                .with(Vehicle::setFuelType, FuelType.PLASMA)
                .build();

        assertEquals(1, v.getId());
        assertEquals("TestCar", v.getName());
        assertEquals(200f, v.getEnginePower());
        assertEquals(4L, v.getNumberOfWheels());
        assertEquals(VehicleType.MOTORCYCLE, v.getType());
        assertEquals(FuelType.PLASMA, v.getFuelType());
    }

    @Test
    @DisplayName("of() returns new builder and build() calls supplier")
    void ofCreatesSupplier() {
        GenericBuilder<Coordinates> b = GenericBuilder.of(Coordinates::new);
        Coordinates c1 = b.build();
        Coordinates c2 = b.build();

        assertNotSame(c1, c2);
    }

    @Test
    @DisplayName("last .with() for same field wins")
    void lastWithWins() {
        Coordinates c = GenericBuilder.of(Coordinates::new)
                .with(Coordinates::setX, 1.0)
                .with(Coordinates::setX, 99.0)
                .build();
        assertEquals(99.0, c.getX());
    }
}
