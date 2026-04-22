package org.ivanrevich.network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {
    private DatagramChannel channel;
    public Client() throws Exception{
        this.channel = DatagramChannel.open();
        this.channel.bind(null); ///
    }

    public void sendMessage(String msg, SocketAddress address) throws Exception{
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        channel.send(buffer, address);
    }
}
