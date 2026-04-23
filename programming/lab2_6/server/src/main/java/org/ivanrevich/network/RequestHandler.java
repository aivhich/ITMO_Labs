package org.ivanrevich.network;

import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.RequestDeserializer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class RequestHandler {
    public static Request apply(SelectionKey key) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(65536);

        DatagramChannel dc = (DatagramChannel) key.channel();
        readBuffer.clear();
        SocketAddress clientAddr = dc.receive(readBuffer); // получаем данные и адрес отправителя
        if (clientAddr == null) return null;
        readBuffer.flip();

        try {
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            return RequestDeserializer.deserialize(bytes);
        } catch (Exception e) {
            System.err.println("Deserialization error: " + e.getMessage());
            return null;
        }
    }
}
