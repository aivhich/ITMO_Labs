package org.ivanrevich.network;

import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.Deserializer;
import org.ivanrevich.utils.Serializer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    private DatagramSocket socket;
    private SocketAddress remoteServer;

    public Client(SocketAddress remoteServer) throws IOException {
        this.remoteServer = remoteServer;
        this.socket = new DatagramSocket();
        socket.connect(remoteServer);
    }

    public Response<?> sendObject(Request<?> request) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Serializer serializer = new Serializer();
        buffer.put(serializer.serialize(request));
        buffer.flip();
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.limit());
        socket.send(packet);

        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        DatagramPacket packet2 = new DatagramPacket(buffer2.array(), buffer2.limit());
        socket.receive(packet2);
        return (Response<?>) (new Deserializer()).deserialize(packet2.getData());
    }

    public void sendMessage(String message) throws IOException {
        byte[] out = message.getBytes();
        DatagramPacket packet = new DatagramPacket(out, out.length, remoteServer);
        socket.send(packet);
    }
}
