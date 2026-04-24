package org.ivanrevich.network;

import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.Deserializer;
import org.ivanrevich.utils.Serializer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class RequestHandler {
    public static ReadResult apply(SelectionKey key) throws IOException {
        DatagramChannel dc = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(65536);  // достаточно для UDP-датаграммы
        InetSocketAddress sender = (InetSocketAddress) dc.receive(buffer);
        if (sender == null) {
            return null;  // нет данных, хотя и странно для OP_READ
        }
        buffer.flip();
        Request<?> request = null;  // предположим, что десериализатор корректный
        try {
            request = (Request<?>) new Deserializer<>().deserialize(buffer.array());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ReadResult(request, sender);
    }
}