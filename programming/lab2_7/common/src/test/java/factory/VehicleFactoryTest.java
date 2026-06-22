package factory;

import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VehicleFactory Tests")
class VehicleFactoryTest {

    /**
     * createVehicle() asks, in order:
     * 1. name
     * 2. enginePower
     * 3. numberOfWheels
     * 4. type (int 1-4)
     * 5. fuelType (int 1-5)
     * 6. coordinates.x (via CoordinatesFactory)
     * 7. coordinates.y (via CoordinatesFactory)
     */
    @Test
    @DisplayName("createVehicle() with all valid input builds a fully populated Vehicle")
    void createVehicleHappyPath() {
        ScriptedIOManager io = new ScriptedIOManager(
                "Falcon",   // name
                "350.5",    // enginePower
                "6",        // numberOfWheels
                "2",        // type -> MOTORCYCLE
                "3",        // fuelType -> NUCLEAR
                "10.0",     // coord x
                "20.0"      // coord y
        );
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle v = factory.createVehicle();

        assertEquals("Falcon", v.getName());
        assertEquals(350.5f, v.getEnginePower());
        assertEquals(6L, v.getNumberOfWheels());
        assertEquals(VehicleType.MOTORCYCLE, v.getType());
        assertEquals(FuelType.NUCLEAR, v.getFuelType());
        assertEquals(10.0, v.getCoordinates().getX());
        assertEquals(20.0f, v.getCoordinates().getY());
        assertNotNull(v.getCreationDate());
        assertEquals(0, v.getAuthorId()); // default placeholder per implementation
    }

    @Test
    @DisplayName("createVehicle() retries on invalid name (empty) before accepting valid")
    void createVehicleRetriesOnInvalidName() {
        ScriptedIOManager io = new ScriptedIOManager(
                "",          // invalid empty name
                "ValidName", // valid name
                "100",       // enginePower
                "4",         // wheels
                "1",         // type -> HELICOPTER
                "1",         // fuelType -> KEROSENE
                "0",         // x
                "0"          // y
        );
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle v = factory.createVehicle();

        assertEquals("ValidName", v.getName());
    }

    @Test
    @DisplayName("createVehicle() retries on out-of-range enginePower (0 is invalid)")
    void createVehicleRetriesOnInvalidEnginePower() {
        ScriptedIOManager io = new ScriptedIOManager(
                "Car",
                "0",     // invalid (must be > 0)
                "150",   // valid
                "4",
                "3",
                "5",
                "0",
                "0"
        );
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle v = factory.createVehicle();

        assertEquals(150f, v.getEnginePower());
    }

    @Test
    @DisplayName("createVehicle() selects correct VehicleType by index")
    void createVehicleSelectsType() {
        ScriptedIOManager io = new ScriptedIOManager(
                "Hover", "100", "2", "4", "5", "0", "0" // type=4 -> HOVERBOARD, fuel=5 -> ANTIMATTER
        );
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle v = factory.createVehicle();

        assertEquals(VehicleType.HOVERBOARD, v.getType());
        assertEquals(FuelType.ANTIMATTER, v.getFuelType());
    }

    @Test
    @DisplayName("updateVehicle() with all blank answers keeps the old field values")
    void updateVehicleKeepsOldOnBlankAnswers() {
        Vehicle old = new Vehicle();
        old.setId(5);
        old.setName("OldName");
        old.setEnginePower(99f);
        old.setNumberOfWheels(3L);
        old.setCoordinates(new Coordinates(1.0, 2f));
        old.setCreationDate(new Date(0));
        old.setType(VehicleType.CHOPPER);
        old.setFuelType(FuelType.MANPOWER);

        // name(blank), enginePower(blank), wheels(blank), type(blank->non-int causes null -> old),
        // fuelType(blank), coord x(blank), coord y(blank)
        ScriptedIOManager io = new ScriptedIOManager("", "", "", "", "", "", "");
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle updated = factory.updateVehicle(old);

        assertEquals(5, updated.getId());
        assertEquals("OldName", updated.getName());
        assertEquals(99f, updated.getEnginePower());
        assertEquals(3L, updated.getNumberOfWheels());
        assertEquals(VehicleType.CHOPPER, updated.getType());
        assertEquals(FuelType.MANPOWER, updated.getFuelType());
        assertEquals(old.getCreationDate(), updated.getCreationDate());
    }

    @Test
    @DisplayName("updateVehicle() overwrites fields when new valid values are given")
    void updateVehicleOverwritesWithNewValues() {
        Vehicle old = new Vehicle();
        old.setId(1);
        old.setName("Old");
        old.setEnginePower(10f);
        old.setNumberOfWheels(2L);
        old.setCoordinates(new Coordinates(0.0, 0f));
        old.setCreationDate(new Date(0));
        old.setType(VehicleType.HELICOPTER);
        old.setFuelType(FuelType.KEROSENE);

        ScriptedIOManager io = new ScriptedIOManager(
                "NewName",  // name
                "500",      // enginePower
                "8",        // wheels
                "3",        // type -> CHOPPER
                "4",        // fuelType -> PLASMA
                "99",       // x
                "1"         // y
        );
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle updated = factory.updateVehicle(old);

        assertEquals("NewName", updated.getName());
        assertEquals(500f, updated.getEnginePower());
        assertEquals(8L, updated.getNumberOfWheels());
        assertEquals(VehicleType.CHOPPER, updated.getType());
        assertEquals(FuelType.PLASMA, updated.getFuelType());
        assertEquals(99.0, updated.getCoordinates().getX());
        assertEquals(1.0f, updated.getCoordinates().getY());
        // id and creationDate preserved from old
        assertEquals(1, updated.getId());
        assertEquals(old.getCreationDate(), updated.getCreationDate());
    }

    @Test
    @DisplayName("createVehicleForRef() builds a partial Vehicle with only enginePower and wheels")
    void createVehicleForRefBuildsPartialVehicle() {
        ScriptedIOManager io = new ScriptedIOManager("250", "4");
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle ref = factory.createVehicleForRef();

        assertEquals(250f, ref.getEnginePower());
        assertEquals(4L, ref.getNumberOfWheels());
        assertNull(ref.getName());
        assertNull(ref.getCoordinates());
    }

    @Test
    @DisplayName("createVehicleForRef() retries on invalid wheel count")
    void createVehicleForRefRetriesOnInvalidWheels() {
        ScriptedIOManager io = new ScriptedIOManager("100", "-5", "10");
        VehicleFactory factory = new VehicleFactory(io);

        Vehicle ref = factory.createVehicleForRef();

        assertEquals(10L, ref.getNumberOfWheels());
    }
}
