package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда удаления и вывода первого элемента коллекции.
 * <p>
 * Извлекает первый элемент из приоритетной очереди и выводит его.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 */
public class RemoveHead implements Command{
    private final ManagersLocator managersLocator;

    public RemoveHead(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "remove_head: print the first element of the collection and remove it";
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);

        try {
            Response<Vehicle> r = (Response<Vehicle>) client.sendObject(new Request<>(CommandType.REMOVE_HEAD, null));
            ioManager.write(r.getBody().toString());
            return r.getResultCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
