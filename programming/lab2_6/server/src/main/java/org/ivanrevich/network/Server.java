package org.ivanrevich.network;

import org.ivanrevich.commands.Result;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.managers.CommandManagerImpl;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.Deserializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class Server {
    private final DatagramChannel channel;
    private Selector selector;
    private boolean running;
    private ManagersLocator managersLocator;

    public Server(int port, ManagersLocator managersLocator) throws Exception {
        selector = Selector.open();
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        this.managersLocator = managersLocator;
        channel.register(selector, SelectionKey.OP_READ);
    }

    public void run() throws Exception {
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        running = true;
        ByteBuffer readBuffer = ByteBuffer.allocate(65536); // временный буфер для чтения датаграммы

        while (running) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next();
                iter.remove();

                if (!key.isValid()) continue;

                if (key.isReadable()) {
                    Request r = RequestHandler.apply(key);
                    Result result = commandManager.run(r);



                    // Подготовка ответа (эхо)

                }
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        if (selector != null) selector.close();
        if (channel != null) channel.close();
    }
}