package org.ivanrevich.utils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
public class Fragment {
    private final List<ByteBuffer> chunks = new ArrayList<>();
    private final int size;
    private int index = 0;

    public Fragment(ByteBuffer buffer, int chunkSize) {
        this.size = buffer.limit(); // ❗ НЕ capacity

        for (int i = 0; i < buffer.limit(); i += chunkSize) {
            int end = Math.min(i + chunkSize, buffer.limit());

            ByteBuffer duplicate = buffer.duplicate();
            duplicate.position(i);
            duplicate.limit(end);

            chunks.add(duplicate.slice());
        }
    }

    public ByteBuffer send() {
        if (index >= chunks.size()) {
            return null;
        }
        return chunks.get(index++);
    }

    public int getChunksCount() {
        return chunks.size();
    }

    public int getDataSize() {
        return size;
    }
}