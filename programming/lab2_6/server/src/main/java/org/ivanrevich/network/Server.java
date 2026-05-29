package org.ivanrevich.network;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.responses.Result;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final DatagramChannel channel;
    private final Selector selector;
    private boolean running;
    private boolean isRawMode;
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
        running = true;

        logger.log(Level.INFO, "Сервер начал прослушивание входящих запросов");
        new Thread(() -> {
            while (running) {
                IOManager ioManager = managersLocator.get(IOManager.class);
                try {
                    String cmd = ioManager.read();
                    if (cmd == null) break;
                    if (!cmd.isEmpty()) {
                        commandManager.run(cmd);
                    }
                } catch (Exception e) {
                    if(e.getMessage().equals(ResultCode.COMMAND_CANCELLED.toString())){
                        commandManager.run("save");
                        System.exit(0);
                    }
                    try {
                        ///  Сделать покрасивше
                        switch (ResultCode.fromMessage(e.getMessage())) {
                            case COMMAND_CANCELLED, COMMAND_SOFT_CANCELLED -> {
                                if (isRawMode) {
                                    ioManager.write("Command cancelled");
                                }
                            }
                            case SCRIPT_END -> {
                                IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                                stack.pop();
                                managersLocator.register(IOManager.class, stack.current());
                            }
                            case SCRIPT_ERROR -> {
                                IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                                stack.pop();
                                ioManager.write("");
                                managersLocator.register(IOManager.class, stack.current());
                                ioManager.write("Script execute error");
                            }

                            case COMMAND_NOT_FOUND -> ioManager.write("Such command not found");
                            case COMMAND_PARSE_ERROR -> ioManager.write("Command parse error");
                            case RECURRENT_SCRIPT_ERROR -> ioManager.write("You're trying to start recurrent scripts");
                            case MANY_INCORRECT_ATTEMPTS ->
                                    ioManager.write("You're trying to enter incorrect data so many times");
                            case INVALID_NUM_OF_ARGS -> ioManager.write("Invalid number of arguments");
                            case INVALID_ARGS -> ioManager.write("Invalid arguments");
                            case ID_ISN_EXIST -> ioManager.write("Element with such id is not exists");
                            case FILE_NOT_FOUND -> ioManager.write("File unreachable. Check file permission and path");
                            default -> ioManager.write(e.getMessage());
                        }
                    }catch (Exception e2){
                        ioManager.write(e2.getMessage());
                    }
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
        stop();
    }

    public void stop() throws IOException {
        if(!running) return;
        running = false;
        logger.log(Level.INFO, "Остановка сервера...");
        if (selector != null) selector.close();
        if (channel != null) channel.close();
        logger.log(Level.INFO, "Сервер остановлен");
    }
}