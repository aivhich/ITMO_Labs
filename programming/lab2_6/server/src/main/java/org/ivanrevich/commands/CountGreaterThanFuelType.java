package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;

import java.util.PriorityQueue;

import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;


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
    public Result<?> run(Request<?> request) {
        //TODO if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);

        try {
            FuelType fuelType = (FuelType) request.getArgs();//FuelType.valueOf(args[0]);
            QueueManager queueManager = managersLocator.get(QueueManager.class);
//            IOManager ioManager = managersLocator.get(IOManager.class);

            PriorityQueue<Vehicle> vehicleList =  queueManager.getAll();
            long count = vehicleList.stream()
                    .filter(vehicle -> vehicle.getFuelType().ordinal()>fuelType.ordinal())
                    .count();

//            ioManager.write(String.format("There %s fuel type greater that it", count));
            return new Result<>(ResultCode.SUCCESS, "Success", count);
        } catch (IllegalArgumentException e) {
            return new Result<>(ResultCode.INVALID_ARGS, "Fail", "Invalid arguments apply to command.");
        }
    }

    @Override
    public Result<?> run(String[] args) {
        if(args.length!=1) throw new AppException(ResultCode.INVALID_NUM_OF_ARGS);

        try {
            FuelType fuelType = FuelType.valueOf(args[0]);
            QueueManager queueManager = managersLocator.get(QueueManager.class);
            IOManager ioManager = managersLocator.get(IOManager.class);

            PriorityQueue<Vehicle> vehicleList =  queueManager.getAll();
            long count = vehicleList.stream()
                    .filter(vehicle -> vehicle.getFuelType().ordinal()>fuelType.ordinal())
                    .count();

            ioManager.write(String.format("There %s fuel type greater that it", count));
            return new Result<>(ResultCode.SUCCESS, "Success", count);
        } catch (IllegalArgumentException e) {
            return new Result<>(ResultCode.INVALID_ARGS, "Fail", "Invalid arguments apply to command.");
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
