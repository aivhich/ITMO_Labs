package org.ivanrevich.network;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.responses.Result;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        logger.log(Level.INFO, "Сетевой канал открыт, порт: " + port);
    }

    public void run() throws Exception {
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);
        running = true;

        logger.log(Level.INFO, "Сервер начал прослушивание входящих запросов");
        new Thread(() -> {
            while (running) {
                try {
                    String cmd = ioManager.read();

                    if (cmd == null) break;
                    if (!cmd.isEmpty())
                        commandManager.run(cmd);

                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }
            }
        }).start();
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
                            logger.log(Level.WARNING, "Получен пустой запрос, пропускаем");
                            continue;
                        }

                        Request<?> request = readResult.request();
                        sender = readResult.senderAddress();

                        logger.log(Level.INFO, "Получен запрос от " + sender
                                + " | команда: " + request.getCommandType());

                        try {
                            Result<?> result = commandManager.run(request);

                            logger.log(Level.INFO, "Команда " + request.getCommandType()
                                    + " выполнена | статус: " + result.getResultCode()
                                    + " | клиент: " + sender);

                            ResponseHandler.apply(key, result, sender);

                            logger.log(Level.INFO, "Ответ отправлен клиенту " + sender);

                        } catch (Exception e) {
                            logger.log(Level.WARNING, "Ошибка при обработке команды "
                                    + request.getCommandType() + ": " + e.getMessage(), e);

                            if (sender != null) {
                                try {
                                    ResponseHandler.apply(key,
                                            new Result<>(
                                                    ResultCode.INTERNAL_SERVER_ERROR,
                                                    e.getMessage(),
                                                    e.getCause()),
                                            sender
                                    );
                                    logger.log(Level.INFO, "Ответ об ошибке отправлен клиенту " + sender);
                                } catch (IOException sendEx) {
                                    logger.log(Level.SEVERE, "Не удалось отправить ответ об ошибке клиенту "
                                            + sender + ": " + sendEx.getMessage());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public void stop() throws IOException {
        running = false;
        logger.log(Level.INFO, "Остановка сервера...");
        if (selector != null) selector.close();
        if (channel != null) channel.close();
        logger.log(Level.INFO, "Сервер остановлен");
    }
}