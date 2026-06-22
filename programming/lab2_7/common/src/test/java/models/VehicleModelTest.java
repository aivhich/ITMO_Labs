package models;

import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Vehicle Model Tests")
class VehicleModelTest {

    private Vehicle buildVehicle(int id, float enginePower, long wheels) {
        Vehicle v = new Vehicle();
        v.setId(id);
        v.setName("Vehicle-" + id);
        v.setEnginePower(enginePower);
        v.setNumberOfWheels(wheels);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        return v;
    }

    // ─── compareTo ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("compareTo (enginePower then numberOfWheels)")
    class CompareTo {

        @Test
        @DisplayName("lower enginePower → negative result")
        void lowerEnginePower() {
            Vehicle a = buildVehicle(1, 50f, 4);
            Vehicle b = buildVehicle(2, 100f, 4);
            assertTrue(a.compareTo(b) < 0);
        }

        @Test
        @DisplayName("higher enginePower → positive result")
        void higherEnginePower() {
            Vehicle a = buildVehicle(1, 200f, 4);
            Vehicle b = buildVehicle(2, 100f, 4);
            assertTrue(a.compareTo(b) > 0);
        }

        @Test
        @DisplayName("equal enginePower, fewer wheels → negative result")
        void equalPowerFewerWheels() {
            Vehicle a = buildVehicle(1, 100f, 2);
            Vehicle b = buildVehicle(2, 100f, 4);
            assertTrue(a.compareTo(b) < 0);
        }

        @Test
        @DisplayName("equal enginePower, more wheels → positive result")
        void equalPowerMoreWheels() {
            Vehicle a = buildVehicle(1, 100f, 6);
            Vehicle b = buildVehicle(2, 100f, 4);
            assertTrue(a.compareTo(b) > 0);
        }

        @Test
        @DisplayName("identical enginePower and wheels → zero")
        void identical() {
            Vehicle a = buildVehicle(1, 100f, 4);
            Vehicle b = buildVehicle(2, 100f, 4);
            assertEquals(0, a.compareTo(b));
        }
    }

    // ─── equals / hashCode ────────────────────────────────────────────────────

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("same content → equal")
        void sameContentEqual() {
            Date d = new Date(0);
            Vehicle a = new Vehicle();
            a.setId(1); a.setName("X"); a.setEnginePower(1f); a.setNumberOfWheels(1);
            a.setCoordinates(new Coordinates(0.0, 0f)); a.setCreationDate(d);
            a.setType(VehicleType.MOTORCYCLE); a.setFuelType(FuelType.NUCLEAR); a.setAuthorId(5);

            Vehicle b = new Vehicle();
            b.setId(1); b.setName("X"); b.setEnginePower(1f); b.setNumberOfWheels(1);
            b.setCoordinates(new Coordinates(0.0, 0f)); b.setCreationDate(d);
            b.setType(VehicleType.MOTORCYCLE); b.setFuelType(FuelType.NUCLEAR); b.setAuthorId(5);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("different id → not equal")
        void differentIdNotEqual() {
            Vehicle a = buildVehicle(1, 100f, 4);
            Vehicle b = buildVehicle(2, 100f, 4);
            // equals checks name, coordinates, etc. but not id explicitly—ids differ only via field
            // so they may still differ, confirm:
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("not equal to null")
        void notEqualToNull() {
            assertNotEquals(null, buildVehicle(1, 100f, 4));
        }

        @Test
        @DisplayName("not equal to different type")
        void notEqualToDifferentType() {
            assertNotEquals("string", buildVehicle(1, 100f, 4));
        }
    }

    // ─── toCsvString ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("toCsvString")
    class ToCsvString {

        @Test
        @DisplayName("all fields present in CSV output")
        void allFieldsPresent() {
            Vehicle v = buildVehicle(42, 350f, 4);
            v.setAuthorId(7);
            String csv = v.toCsvString();
            assertTrue(csv.startsWith("42,"));
            assertTrue(csv.contains("Vehicle-42"));
            assertTrue(csv.contains("350.0"));
            assertTrue(csv.contains("HELICOPTER"));
            assertTrue(csv.contains("KEROSENE"));
            assertTrue(csv.contains(",7"));
        }

        @Test
        @DisplayName("name with comma is escaped")
        void nameWithCommaEscaped() {
            Vehicle v = buildVehicle(1, 100f, 2);
            v.setName("A,B");
            String csv = v.toCsvString();
            assertTrue(csv.contains("\"A,B\""));
        }

        @Test
        @DisplayName("name with double-quote is escaped")
        void nameWithQuoteEscaped() {
            Vehicle v = buildVehicle(1, 100f, 2);
            v.setName("A\"B");
            String csv = v.toCsvString();
            assertTrue(csv.contains("\"A\"\"B\""));
        }
    }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toString contains id and name")
    void toStringContainsIdAndName() {
        Vehicle v = buildVehicle(99, 200f, 4);
        String s = v.toString();
        assertTrue(s.contains("99"));
        assertTrue(s.contains("Vehicle-99"));
    }
}
