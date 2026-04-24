package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда очистки коллекции.
 * <p>
 * Удаляет все элементы из коллекции транспортных средств.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class Clear implements Command{
    private final ManagersLocator managersLocator;

    @Override
    public ResultCode run(String[] args) {
        //QueueManager queueManager = managersLocator.get(QueueManager.class);
        //queueManager.clear();

        Client client = managersLocator.get(Client.class);
        try {
            Response r = client.sendObject(new Request<>(CommandType.CLEAR, null));
            return r.getResultCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "clear: clear collection";
    }

    public Clear(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }
}
