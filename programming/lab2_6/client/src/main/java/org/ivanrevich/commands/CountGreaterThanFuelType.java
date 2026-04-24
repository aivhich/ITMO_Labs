package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Команда подсчёта элементов с типом топлива больше заданного.
 * <p>
 * Сравнивает ordinal значения {@link FuelType} и выводит количество
 * элементов, у которых тип топлива больше указанного.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see FuelType
 * @see Vehicle
 */
public class CountGreaterThanFuelType implements Command{
    private final ManagersLocator managersLocator;

    @Override
    public ResultCode run(String[] args) {
        if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);

        try {
            FuelType fuelType = FuelType.valueOf(args[0]);
            IOManager ioManager = managersLocator.get(IOManager.class);
            Client client = managersLocator.get(Client.class);

            Response<?> response = client.sendObject(new Request<>(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, fuelType));
            List<Vehicle> vehicleList = (List<Vehicle>) response.getBody();

            long count = vehicleList.stream()
                    .filter(vehicle -> vehicle.getFuelType().ordinal()>fuelType.ordinal())
                    .count();

            ioManager.write(String.format("There %s fuel type greater that it", count));
            return ResultCode.SUCCESS;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(Exceptions.INVALID_ARGS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "count_greater_than_fuel_type fuelType: display the number of elements whose fuelType field value is greater than the specified value";
    }

    public CountGreaterThanFuelType(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }
}
