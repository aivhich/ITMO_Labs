package org.ivanrevich.network;

import org.ivanrevich.responses.Result;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Set;
import java.util.logging.Logger;

import java.util.logging.Level;

public class Server {
    private final DatagramChannel channel;
    private final Selector selector;
    private boolean running;
    private final ManagersLocator managersLocator;
    private final Logger logger = Logger.getLogger(Server.class.getName());

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
                    InetSocketAddress sender;
                    
                    ReadResult readResult = RequestHandler.apply(key);
                    if (readResult == null) continue;

                    Request<?> request = readResult.request();
                    sender = readResult.senderAddress();

                    try{
                        Result<?> result = commandManager.run(request);
                        ResponseHandler.apply(key, result, sender);
                    }catch (Exception e){
                        logger.log(Level.WARNING, e.getMessage());
                        if(sender!=null)
                            ResponseHandler.apply(key,
                                    new Result<>(
                                            ResultCode.INTERNAL_SERVER_ERROR,
                                            e.getMessage(),
                                            e.getCause()),
                                    sender
                            );
                    }
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