package org.ivanrevich.network;

import org.ivanrevich.responses.Result;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class ResponseHandler {
    public static void apply(SelectionKey key, Result<?> result) throws IOException {
        DatagramChannel dc = (DatagramChannel) key.channel();

        ByteBuffer data = (new Serializer()).serialize(new Response<>(result.getResultCode(), result.getMessage(), result.getOutput()));

        // Отправка ответа сразу (UDP не требует OP_WRITE, можно писать напрямую)
        dc.send(data, dc.getRemoteAddress());
    }
}
