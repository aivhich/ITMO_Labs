package org.ivanrevich.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Objects;

public class Server {
    private final DatagramChannel channel;
    private boolean running;


    public Server(int port) throws Exception{
        this.channel = DatagramChannel.open();
        //this.channel.configureBlocking(false);
        this.channel.bind(new InetSocketAddress(port));
    }


    public void run() throws Exception {
        running = true;
        while(running){
            if(Objects.equals(receive(), "stop")){
                running = false;
            }
        }
    }


    public void disconnect(SocketAddress address) throws Exception{
        channel.disconnect();
    }
    public void close() throws Exception{
        channel.close();
    }
    public void send(String message) throws Exception{
    }

    private static String extractMessage(ByteBuffer buffer){
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String msg = new String(bytes);
        return msg;
    }

    public String receive() throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress address = channel.receive(buffer);
        String message = extractMessage(buffer);
        System.out.println("Client with address "+address+"say"+message);
        return message;
    }
}
