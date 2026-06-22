package factory;

import org.ivanrevich.factory.CoordinatesFactory;
import org.ivanrevich.models.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoordinatesFactory Tests")
class CoordinatesFactoryTest {

    @Test
    @DisplayName("createCoordinates() with valid input builds correct Coordinates")
    void createWithValidInput() {
        ScriptedIOManager io = new ScriptedIOManager("10.5", "20.5");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates c = factory.createCoordinates();

        assertEquals(10.5, c.getX());
        assertEquals(20.5f, c.getY());
    }

    @Test
    @DisplayName("createCoordinates() retries on invalid x before accepting valid value")
    void createRetriesOnInvalidX() {
        ScriptedIOManager io = new ScriptedIOManager("-500", "0", "1.0");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates c = factory.createCoordinates();

        assertEquals(0.0, c.getX());
        assertEquals(1.0f, c.getY());
    }

    @Test
    @DisplayName("createCoordinates() retries on invalid y before accepting valid value")
    void createRetriesOnInvalidY() {
        ScriptedIOManager io = new ScriptedIOManager("5.0", "500", "100");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates c = factory.createCoordinates();

        assertEquals(5.0, c.getX());
        assertEquals(100f, c.getY());
    }

    @Test
    @DisplayName("updateCoordinates() with empty input keeps old x and y")
    void updateWithEmptyInputKeepsOld() {
        Coordinates old = new Coordinates(42.0, 24f);
        ScriptedIOManager io = new ScriptedIOManager("", "");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates updated = factory.updateCoordinates(old);

        assertEquals(42.0, updated.getX());
        assertEquals(24f, updated.getY());
    }

    @Test
    @DisplayName("updateCoordinates() with new values overwrites old")
    void updateWithNewValues() {
        Coordinates old = new Coordinates(1.0, 1f);
        ScriptedIOManager io = new ScriptedIOManager("99.9", "88.8");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates updated = factory.updateCoordinates(old);

        assertEquals(99.9, updated.getX());
        assertEquals(88.8f, updated.getY());
    }

    @Test
    @DisplayName("createCoordinates() accepts comma as decimal separator")
    void createWithCommaDecimal() {
        ScriptedIOManager io = new ScriptedIOManager("3,5", "2,5");
        CoordinatesFactory factory = new CoordinatesFactory(io);

        Coordinates c = factory.createCoordinates();

        assertEquals(3.5, c.getX());
        assertEquals(2.5f, c.getY());
    }
}
