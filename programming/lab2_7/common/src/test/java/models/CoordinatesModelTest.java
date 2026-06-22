package models;

import org.ivanrevich.models.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coordinates Model Tests")
class CoordinatesModelTest {

    @Test
    @DisplayName("no-arg constructor creates null fields")
    void noArgConstructor() {
        Coordinates c = new Coordinates();
        assertNull(c.getX());
        assertNull(c.getY());
    }

    @Test
    @DisplayName("all-arg constructor sets fields correctly")
    void allArgConstructor() {
        Coordinates c = new Coordinates(10.0, 20f);
        assertEquals(10.0, c.getX());
        assertEquals(20f, c.getY());
    }

    @Test
    @DisplayName("setX / setY mutators work")
    void setters() {
        Coordinates c = new Coordinates();
        c.setX(-100.0);
        c.setY(300f);
        assertEquals(-100.0, c.getX());
        assertEquals(300f, c.getY());
    }

    @Test
    @DisplayName("toString contains x and y values")
    void toStringContainsValues() {
        Coordinates c = new Coordinates(5.5, 3.3f);
        String s = c.toString();
        assertTrue(s.contains("5.5"));
        assertTrue(s.contains("3.3"));
    }
}
