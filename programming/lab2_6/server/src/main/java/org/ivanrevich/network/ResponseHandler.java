package org.ivanrevich.network;

import org.ivanrevich.utils.FragmentInfo;
import org.ivanrevich.utils.Fragment;
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
        Fragment fragment = new Fragment(data, 1024);
        FragmentInfo fragmentInfo = new FragmentInfo(fragment.getDataSize(), fragment.getChunksCount(), 1024);

        dc.send(serializer.serialize(fragmentInfo), clientAddress);
        while(true){
            ByteBuffer buffer = fragment.send();
            if(buffer == null) break;
            dc.send(buffer, clientAddress);
        }
    }
}