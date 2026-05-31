package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.List;
/**
 * Команда вывода элементов коллекции в порядке возрастания.
 * <p>
 * Сортирует коллекцию по натуральному порядку {@link Vehicle#compareTo(Vehicle)}
 * и выводит все элементы.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 * @see Comparable
 */
public class PrintAscending implements Command{
    private final ManagersLocator managersLocator;

    public PrintAscending(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        //QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);

        try {
            Response<List<Vehicle>> r = (Response<List<Vehicle>>) client.sendObject(new Request<>(CommandType.PRINT_ASCENDING, null));
            for(Vehicle v: r.getBody()){
                ioManager.write(v.toString());
            }
            return r.getResultCode();
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        }
    }

    @Override
    public String toString() {
        return "print_ascending: display collection elements in ascending order";
    }
}
