package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.Map;


/**
 * Команда вывода информации о коллекции.
 * <p>
 * Выводит тип коллекции, количество элементов и дату создания первого элемента.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class Info implements Command{
    private final ManagersLocator managersLocator;

    public Info(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);

        try {
            Response<?> r = client.sendObject(new Request<>(CommandType.INFO, null));
            Map<String, String> map = (Map<String, String>) r.getBody();
            io.write("Collection type: " + map.get("type"));
            io.write("Number of elements: " + map.get("count"));
            io.write("Init date: " + map.get("init_date"));
            return r.getResultCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "info: print information about the collection (type, initialization date, number of elements, etc.) to the standard output stream.";
    }
}
