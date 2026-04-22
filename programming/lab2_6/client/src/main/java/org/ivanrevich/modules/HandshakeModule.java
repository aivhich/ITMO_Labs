package org.ivanrevich.modules;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class HandshakeModule {
    private DatagramChannel dataChannel;

    public HandshakeModule(int port) throws Exception{
        this.dataChannel = DatagramChannel.open();
        this.dataChannel.bind(new InetSocketAddress(port));
    }

    public DatagramChannel getDataChannel() {
        return dataChannel;
    }
}
