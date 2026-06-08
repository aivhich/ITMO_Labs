package org.ivanrevich.network;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.exceptions.ErrorHandler;
import org.ivanrevich.managers.*;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final DatagramChannel channel;
    private final Selector selector;
    private boolean running;
    private boolean isRawMode;
    private final ManagersLocator managersLocator;

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService handlePool = Executors.newFixedThreadPool(4);
    private final ExecutorService answerPool = Executors.newFixedThreadPool(4);

    private final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(int port, ManagersLocator managersLocator) throws Exception {
        selector = Selector.open();
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        this.managersLocator = managersLocator;

        logger.log(Level.INFO, "Net channel is open, port: " + port);
    }

    public void run() throws Exception {
        CommandManager commandManager = managersLocator.get(CommandManager.class);

        running = true;

        logger.log(Level.INFO, "Server start to listen requests");
        Thread consoleThread = new Thread(() -> {
            IOManager ioManager = managersLocator.get(IOManager.class);
            ErrorHandler errorHandler = new ServerErrorHandler(managersLocator, ioManager, isRawMode);
            while (running) {
                try {
                    String cmd = ioManager.read();
                    if (cmd == null) break;
                    if (!cmd.isEmpty()) {
                        commandManager.run(cmd);
                    }
                } catch (AppException e) {
                    if(e.getMessage().equals(ResultCode.COMMAND_CANCELLED.toString())){
                        commandManager.run("save");
                        System.exit(0);
                    }
                    try {
                        errorHandler.handle(e);
                    }catch (Exception e2){
                        ioManager.write(e2.getMessage());
                    }
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        while (running) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();

                for (var iter = keys.iterator(); iter.hasNext(); ) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (!key.isValid()) continue;

                    if (key.isReadable()) {
                        InetSocketAddress sender = null;
                        ReadResult readResult = RequestHandler.apply(key);
                        if (readResult == null) {
                            logger.log(Level.WARNING, "Empty request, pass it");
                            return;
                        }

                        readPool.submit(() -> handleRequest(key, readResult, commandManager));
                    }
                }
            } catch (Exception e) {}
        }
        stop();
    }
    private void sendResponse(SelectionKey key, Result<?> result, InetSocketAddress clientAddress) {
        try {
            ResponseHandler.apply(key, result, clientAddress);
            logger.log(Level.INFO, "Answer sent to client " + clientAddress);
        }catch (IOException e){
            if (clientAddress != null) {
                try {
                    ResponseHandler.apply(key,
                            new Result<>(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause()),
                            clientAddress
                    );
                    logger.log(Level.INFO, "Answer sent to client with address " + clientAddress);
                } catch (IOException sendEx) {
                    logger.log(Level.SEVERE, "Something happen when we try send answer to client " + clientAddress + ": " + sendEx.getMessage());
                }
            }
        }
    }

    private void runRequest(SelectionKey key, Request<?> request,
                            InetSocketAddress clientAddress,
                            CommandManager commandManager){
        try {
            Result<?> result = commandManager.run(request);
            logger.log(Level.INFO, "Command " + request.getCommandType() + " done | status: " + result.getResultCode() + " | client: " + clientAddress);
            answerPool.submit(() -> {sendResponse(key, result, clientAddress);});
        } catch (Exception e) {
            logger.log(Level.WARNING, "When we try to run command we get error " + request.getCommandType() + ": " + e.getMessage(), e);
            answerPool.submit(() -> sendResponse(key,
                    new Result<>(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage(), null),
                    clientAddress));
        }
    }

    private void handleRequest(SelectionKey key, ReadResult readResult, CommandManager commandManager){
        try{
            Request<?> request = readResult.request();
            InetSocketAddress sender = readResult.senderAddress();
            final InetSocketAddress clientAddress = sender;
            logger.log(Level.INFO, "Got request from " + sender + " | command: " + request.getCommandType());
            handlePool.submit(() -> runRequest(key, request, clientAddress, commandManager));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error when we try to handle request" + e.getMessage());
        }
    }

    public void stop() throws IOException {
        if(!running) return;
        running = false;
        logger.log(Level.INFO, "Server stopping...");
        if (selector != null) selector.close();
        if (channel != null) channel.close();
        logger.log(Level.INFO, "Server stopped");
    }
}