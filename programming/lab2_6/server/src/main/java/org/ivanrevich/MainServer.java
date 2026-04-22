package org.ivanrevich;

import org.ivanrevich.network.Server;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        server.run();

        /*
        // Serialize
ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutputStream oos = new ObjectOutputStream(baos);
oos.writeObject(myDataInstance);
byte[] data = baos.toByteArray();

// Send via NIO
DatagramChannel channel = DatagramChannel.open();
ByteBuffer buffer = ByteBuffer.wrap(data);
channel.send(buffer, new InetSocketAddress("hostname", port));

        * */
    }
}