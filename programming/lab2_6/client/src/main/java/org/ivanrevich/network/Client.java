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
    private final SocketAddress remoteServer;
    private final Serializer serializer = new Serializer();
    private static final int TIMEOUT_MS = 1000;
    private static final int MAX_RETRIES = 5;

    public Client(SocketAddress remoteServer) throws IOException {
        this.remoteServer = remoteServer;
        this.socket = new DatagramSocket();
        socket.connect(remoteServer);
        socket.setSoTimeout(TIMEOUT_MS);
    }

    public Response<?> sendObject(Request<?> request) throws IOException, ClassNotFoundException {
        IOException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return doSend(request);
            } catch (SocketTimeoutException e) {
                lastException = e;
                System.err.println("[Client] Сервер недоступен, попытка " + attempt + "/" + MAX_RETRIES + "...");
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(1000L * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Прервано во время ожидания повтора", ie);
                    }
                    reconnect();
                }
            }
        }

        throw new IOException("Сервер недоступен после " + MAX_RETRIES + " попыток", lastException);
    }

    private Response<?> doSend(Request<?> request) throws IOException, ClassNotFoundException {
        ByteBuffer data = serializer.serialize(request);
        Fragment fragment = new Fragment(data, 1024);
        FragmentInfo fragmentInfoRequest = new FragmentInfo(fragment.getDataSize(), fragment.getChunksCount(), 1024);

        ByteBuffer infoBuffer = ByteBuffer.allocate(65536);
        infoBuffer.put(serializer.serialize(fragmentInfoRequest));
        infoBuffer.flip();
        socket.send(new DatagramPacket(infoBuffer.array(), infoBuffer.limit()));

        while (true) {
            ByteBuffer buffer = fragment.send();
            if (buffer == null) break;
            socket.send(new DatagramPacket(buffer.array(), buffer.limit()));
        }

        byte[] infoBuf = new byte[65536];
        DatagramPacket infoPacket = new DatagramPacket(infoBuf, infoBuf.length);
        socket.receive(infoPacket);
        byte[] infoData = new byte[infoPacket.getLength()];
        System.arraycopy(infoPacket.getData(), 0, infoData, 0, infoData.length);
        FragmentInfo fragmentInfo = new Deserializer<FragmentInfo>().deserialize(infoData);

        int totalSize = fragmentInfo.getSize();
        byte[] fullData = new byte[totalSize];
        int offset = 0;

        for (int i = 0; i < fragmentInfo.getChunksCount(); i++) {
            int chunkSize = fragmentInfo.getChunksSize();
            byte[] chunkBuf = new byte[chunkSize];
            int packetSize = (chunkSize > totalSize) ? totalSize : chunkSize;
            DatagramPacket chunkPacket = new DatagramPacket(chunkBuf, packetSize);
            socket.receive(chunkPacket);
            int len = chunkPacket.getLength();
            System.arraycopy(chunkPacket.getData(), 0, fullData, offset, len);
            offset += len;
        }

        return (Response<?>) new Deserializer<>().deserialize(fullData);
    }

    private void reconnect() {
        try {
            socket.close();
            socket = new DatagramSocket();
            socket.connect(remoteServer);
            socket.setSoTimeout(TIMEOUT_MS);
        } catch (IOException e) {
            System.err.println("[Client] Ошибка при переподключении: " + e.getMessage());
        }
    }
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}