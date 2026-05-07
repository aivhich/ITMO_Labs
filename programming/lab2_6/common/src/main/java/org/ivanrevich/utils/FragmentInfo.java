package org.ivanrevich.utils;

import java.io.Serializable;

public class FragmentInfo implements Serializable {
    private int size;
    private int chunksCount;

    private int chunksSize;

    public FragmentInfo(int size, int chunksCount, int chunksSize) {
        this.size = size;
        this.chunksCount = chunksCount;
        this.chunksSize = chunksSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getChunksCount() {
        return chunksCount;
    }

    public void setChunksCount(int chunksCount) {
        this.chunksCount = chunksCount;
    }

    public void setChunksSize(int chunksSize) {
        this.chunksSize = chunksSize;
    }

    public int getChunksSize() {
        return chunksSize;
    }
}
