package models;

import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.VehicleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FuelType and VehicleType Enum Tests")
class EnumTests {

    @Test
    @DisplayName("FuelType has 5 values")
    void fuelTypeCount() {
        assertEquals(5, FuelType.values().length);
    }

    @Test
    @DisplayName("FuelType ordinal order: KEROSENE=0, ANTIMATTER=4")
    void fuelTypeOrdinals() {
        assertEquals(0, FuelType.KEROSENE.ordinal());
        assertEquals(4, FuelType.ANTIMATTER.ordinal());
    }

    @Test
    @DisplayName("FuelType NUCLEAR ordinal is greater than MANPOWER")
    void fuelTypeOrdering() {
        assertTrue(FuelType.NUCLEAR.ordinal() > FuelType.MANPOWER.ordinal());
    }

    @Test
    @DisplayName("FuelType valueOf works")
    void fuelTypeValueOf() {
        assertEquals(FuelType.PLASMA, FuelType.valueOf("PLASMA"));
    }

    @Test
    @DisplayName("VehicleType has 4 values")
    void vehicleTypeCount() {
        assertEquals(4, VehicleType.values().length);
    }

    @Test
    @DisplayName("VehicleType valueOf works")
    void vehicleTypeValueOf() {
        assertEquals(VehicleType.HOVERBOARD, VehicleType.valueOf("HOVERBOARD"));
    }

    @Test
    @DisplayName("countGreaterThanFuelType logic: ordinal comparison")
    void ordinalComparison() {
        // NUCLEAR(2) > MANPOWER(1) → true
        assertTrue(FuelType.NUCLEAR.ordinal() > FuelType.MANPOWER.ordinal());
        // KEROSENE(0) > ANTIMATTER(4) → false
        assertFalse(FuelType.KEROSENE.ordinal() > FuelType.ANTIMATTER.ordinal());
    }
}
