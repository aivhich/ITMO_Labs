package org.ivanrevich.network;

import org.ivanrevich.commands.Result;
import org.ivanrevich.responses.Response;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class ResponseHandler {
    public static void apply(SelectionKey key, Result result){
        DatagramChannel dc = (DatagramChannel) key.channel();

        new Response(result.getResultCode(), )
        ByteBuffer writeBuf = data.getWriteBuffer();
        writeBuf.clear();
        String response = "ECHO: " + new String(readBuffer.array(), 0, readBuffer.limit());
        writeBuf.put(response.getBytes());
        writeBuf.flip();

        // Отправка ответа сразу (UDP не требует OP_WRITE, можно писать напрямую)
        dc.send(writeBuf, clientAddr);
    }
}
