package utils;

import org.ivanrevich.utils.FragmentInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FragmentInfo Tests")
class FragmentInfoTest {

    @Test
    @DisplayName("constructor sets all fields correctly")
    void constructor() {
        FragmentInfo info = new FragmentInfo(3000, 3, 1024);
        assertEquals(3000, info.getSize());
        assertEquals(3, info.getChunksCount());
        assertEquals(1024, info.getChunksSize());
    }

    @Test
    @DisplayName("setters update values")
    void setters() {
        FragmentInfo info = new FragmentInfo(0, 0, 0);
        info.setSize(500);
        info.setChunksCount(5);
        info.setChunksSize(100);

        assertEquals(500, info.getSize());
        assertEquals(5, info.getChunksCount());
        assertEquals(100, info.getChunksSize());
    }
}
