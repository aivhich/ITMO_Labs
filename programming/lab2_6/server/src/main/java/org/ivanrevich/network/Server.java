package org.ivanrevich.network;

import org.ivanrevich.responses.Result;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Set;

public class Server {
    private final DatagramChannel channel;
    private final Selector selector;
    private boolean running;
    private final ManagersLocator managersLocator;

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
        while (running) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next();
                iter.remove();

                if (!key.isValid()) continue;

                if (key.isReadable()) {
                    ReadResult readResult = RequestHandler.apply(key);
                    if (readResult == null) continue;

                    Request<?> request = readResult.request();
                    InetSocketAddress sender = readResult.senderAddress();

                    Result<?> result = commandManager.run(request);
                    ResponseHandler.apply(key, result, sender);
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