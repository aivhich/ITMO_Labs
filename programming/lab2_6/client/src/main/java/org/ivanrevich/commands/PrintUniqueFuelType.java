package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.Set;

/**
 * Команда вывода уникальных значений типа топлива.
 * <p>
 * Выводит все уникальные значения поля  из коллекции.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see FuelType
 * @see Vehicle
 */
public class PrintUniqueFuelType implements Command{
    private final ManagersLocator managersLocator;

    public PrintUniqueFuelType(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        //QueueManager queueManager = managersLocator.get(QueueManager.class);
        Client client = managersLocator.get(Client.class);
        try {
            Response<Set<FuelType>> r = (Response<Set<FuelType>>) client.sendObject(new Request<>(CommandType.PRINT_UNIQUE_FUEL_TYPE, null));
            Set<FuelType> uniqueFuelTypes = r.getBody();

            if (uniqueFuelTypes.isEmpty()) {
                io.write("Collection is empty, no fuel types available.");
            } else {
                io.write("Unique fuel types in collection:");
                uniqueFuelTypes.forEach(ft -> io.write(" - " + ft));
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
        return "print_unique_fuel_type: display unique values of the fuelType field for all elements in the collection";
    }
}
