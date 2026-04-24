package org.ivanrevich.network;

import org.ivanrevich.responses.Result;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class ResponseHandler {
    public static void apply(SelectionKey key, Result<?> result, InetSocketAddress clientAddress) throws IOException {
        DatagramChannel dc = (DatagramChannel) key.channel();

        Serializer serializer = new Serializer();
        Response<?> response = new Response<>(
                result.getResultCode(),
                result.getMessage(),
                result.getOutput()
        );
        ByteBuffer data = serializer.serialize(response);

        // UDP: отправляем датаграмму обратно клиенту
        dc.send(data, clientAddress);
    }
}