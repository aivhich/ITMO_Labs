package org.ivanrevich.network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;

class ClientData {
    private final ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(8192);
    private Object data;
    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }
}
