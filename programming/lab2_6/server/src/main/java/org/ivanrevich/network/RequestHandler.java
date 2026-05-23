package org.ivanrevich.network;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.Deserializer;
import org.ivanrevich.utils.FragmentInfo;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class RequestHandler {
    public static ReadResult apply(SelectionKey key) throws IOException {
        DatagramChannel dc = (DatagramChannel) key.channel();

        ByteBuffer infoBuffer = ByteBuffer.allocate(65536);
        InetSocketAddress sender = (InetSocketAddress) dc.receive(infoBuffer);
        if (sender == null) {
            return null;
        }

        infoBuffer.flip();

        FragmentInfo fragmentInfo;
        try {
            fragmentInfo = (FragmentInfo) new Deserializer<>()
                    .deserialize(infoBuffer.array());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int totalSize = fragmentInfo.getSize();
        ByteBuffer fullData = ByteBuffer.allocate(totalSize);

        int received = 0;

        while (received < fragmentInfo.getChunksCount()) {
            ByteBuffer buffer = ByteBuffer.allocate(fragmentInfo.getChunksSize());

            InetSocketAddress addr = (InetSocketAddress) dc.receive(buffer);
            if (addr == null) continue;

            buffer.flip();

            byte[] chunkData = new byte[buffer.remaining()];
            buffer.get(chunkData);
            fullData.put(chunkData);
            received++;
        }

        try {
            return new ReadResult(
                    (Request<?>) new Deserializer<>()
                            .deserialize(fullData.array()),
                    sender
            );
        } catch (ClassNotFoundException e) {
            throw new AppException(ResultCode.INVALID_REQUEST);
        }
    }
}