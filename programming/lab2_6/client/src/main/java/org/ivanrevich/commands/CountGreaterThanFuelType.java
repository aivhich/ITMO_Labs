package org.ivanrevich.commands;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда подсчёта элементов с типом топлива больше заданного.
 * <p>
 * Отправляет запрос на сервер — фильтрация и подсчёт выполняются там.
 * Клиент только выводит полученный результат.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see FuelType
 */
public class CountGreaterThanFuelType implements Command {
    private final ManagersLocator managersLocator;

    public CountGreaterThanFuelType(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        if (args.length != 1) return ResultCode.INVALID_NUM_OF_ARGS;

        try {
            FuelType fuelType = FuelType.valueOf(args[0].toUpperCase());
            IOManager ioManager = managersLocator.get(IOManager.class);
            Client client = managersLocator.get(Client.class);

            Response<?> response = client.sendObject(
                    new Request<>(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, fuelType)
            );

            long count = (long) response.getBody();
            ioManager.write(String.format("Vehicles with fuel type greater than %s: %d", fuelType, count));
            return response.getResultCode();

        } catch (IllegalArgumentException e) {
            return ResultCode.INVALID_ARGS;
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        }
    }

    @Override
    public String toString() {
        return "count_greater_than_fuel_type fuelType: display the number of elements whose fuelType field value is greater than the specified value";
    }
}