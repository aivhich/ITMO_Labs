package org.ivanrevich.network;

import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.FragmentInfo;
import org.ivanrevich.utils.Fragment;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.Deserializer;
import org.ivanrevich.utils.Serializer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    private DatagramSocket socket;
    private SocketAddress remoteServer;
    private
    Serializer serializer = new Serializer();

    public Client(SocketAddress remoteServer) throws IOException {
        this.remoteServer = remoteServer;
        this.socket = new DatagramSocket();
        socket.connect(remoteServer);
    }

    public Response<?> sendObject(Request<?> request) throws IOException, ClassNotFoundException {
        // 1. Send the request as before
        ByteBuffer data = serializer.serialize(request);
        Fragment fragment = new Fragment(data, 1024);
        FragmentInfo fragmentInfoResponse = new FragmentInfo(fragment.getDataSize(), fragment.getChunksCount(), 1024);


        ByteBuffer infoBuffer = ByteBuffer.allocate(data.capacity());
        infoBuffer.put(serializer.serialize(fragmentInfoResponse));
        infoBuffer.flip();
        socket.send(new DatagramPacket(infoBuffer.array(), infoBuffer.limit()));
        while(true){
            ByteBuffer buffer = fragment.send();
            if(buffer == null) break;
            socket.send(new DatagramPacket(buffer.array(), buffer.limit()));
        }



        // 2. Receive FragmentInfoResponse (first datagram)
        byte[] infoBuf = new byte[65536];
        DatagramPacket infoPacket = new DatagramPacket(infoBuf, infoBuf.length);
        socket.receive(infoPacket);
        byte[] infoData = new byte[infoPacket.getLength()];
        System.arraycopy(infoPacket.getData(), 0, infoData, 0, infoData.length);
        FragmentInfo fragmentInfo = (FragmentInfo)
                new Deserializer<>().deserialize(infoData);



        // 3. Allocate array for the full response data
        int totalSize = fragmentInfo.getSize();
        byte[] fullData = new byte[totalSize];
        int offset = 0;

        // 4. Receive each chunk
        for (int i = 0; i < fragmentInfo.getChunksCount(); i++) {
            byte[] chunkBuf = new byte[fragmentInfo.getChunksSize()];
            DatagramPacket chunkPacket;
            if(fragmentInfo.getChunksSize()>totalSize) {
                chunkPacket = new DatagramPacket(chunkBuf, totalSize);
            }else{
                chunkPacket = new DatagramPacket(chunkBuf, chunkBuf.length);
            }
            socket.receive(chunkPacket);
            int len = chunkPacket.getLength();
            System.arraycopy(chunkPacket.getData(), 0, fullData, offset, len);
            offset += len;
        }

        // 5. Deserialize and return the complete Response
        return (Response<?>) new Deserializer<>().deserialize(fullData);
    }
}
