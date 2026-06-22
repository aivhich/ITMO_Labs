package utils;

import org.ivanrevich.utils.Fragment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Fragment Tests")
class FragmentTest {

    @Test
    @DisplayName("data smaller than chunk size → 1 chunk")
    void smallDataOneSingleChunk() {
        byte[] data = "hello".getBytes();
        Fragment f = new Fragment(ByteBuffer.wrap(data), 1024);
        assertEquals(1, f.getChunksCount());
        assertEquals(data.length, f.getDataSize());
    }

    @Test
    @DisplayName("data exactly one chunk → 1 chunk")
    void exactlyOneChunk() {
        byte[] data = new byte[1024];
        Fragment f = new Fragment(ByteBuffer.wrap(data), 1024);
        assertEquals(1, f.getChunksCount());
    }

    @Test
    @DisplayName("data spanning multiple chunks → correct chunk count")
    void multipleChunks() {
        byte[] data = new byte[3000];
        Fragment f = new Fragment(ByteBuffer.wrap(data), 1024);
        // 3000 / 1024 = 2 full + 1 partial = 3 chunks
        assertEquals(3, f.getChunksCount());
        assertEquals(3000, f.getDataSize());
    }

    @Test
    @DisplayName("send() returns non-null for each chunk then null")
    void sendReturnsNullAfterAllChunks() {
        byte[] data = new byte[2048];
        Fragment f = new Fragment(ByteBuffer.wrap(data), 1024);

        assertNotNull(f.send()); // chunk 1
        assertNotNull(f.send()); // chunk 2
        assertNull(f.send());    // exhausted
    }

    @Test
    @DisplayName("send() returns chunks with correct sizes")
    void sendChunkSizes() {
        byte[] data = new byte[1500];
        Fragment f = new Fragment(ByteBuffer.wrap(data), 1024);

        ByteBuffer chunk1 = f.send();
        ByteBuffer chunk2 = f.send();

        assertNotNull(chunk1);
        assertNotNull(chunk2);
        assertEquals(1024, chunk1.remaining());
        assertEquals(476, chunk2.remaining()); // 1500 - 1024
    }

    @Test
    @DisplayName("empty buffer → 0 chunks, getDataSize = 0")
    void emptyBuffer() {
        Fragment f = new Fragment(ByteBuffer.wrap(new byte[0]), 1024);
        assertEquals(0, f.getChunksCount());
        assertEquals(0, f.getDataSize());
        assertNull(f.send());
    }
}
