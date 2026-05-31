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
import java.util.PriorityQueue;

/**
 * Команда вывода всех элементов коллекции.
 * <p>
 * Выводит строковое представление всех транспортных средств в коллекции.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class Show implements Command{
    private ManagersLocator managersLocator;

    public Show(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);

        try {
            Response<?> r = client.sendObject(new Request<>(CommandType.SHOW, null));
            PriorityQueue<Vehicle> vehicles = (PriorityQueue<Vehicle>) r.getBody();
            for(Vehicle v: vehicles) {
                ioManager.write(v.toString());
            }
            if(vehicles.isEmpty()){
                ioManager.write("Priority queue is empty");
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
        return "show : Print all elements of the collection to standard output as strings.";
    }
}
