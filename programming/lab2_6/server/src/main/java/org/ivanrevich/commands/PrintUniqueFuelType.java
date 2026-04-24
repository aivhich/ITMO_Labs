package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.util.Set;
import java.util.stream.Collectors;
import org.ivanrevich.responses.Result;
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
    public Result<?> run(Request<?> r) {
        //IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        Set<FuelType> uniqueFuelTypes = queueManager.getAll().stream()
                .map(Vehicle::getFuelType)
                .collect(Collectors.toSet());

        //if (uniqueFuelTypes.isEmpty()) {
            //io.write("Collection is empty, no fuel types available.");
        //} else {
            //io.write("Unique fuel types in collection:");
            //uniqueFuelTypes.forEach(ft -> io.write(" - " + ft));
        //}

        return new Result<>(ResultCode.SUCCESS, "Success", uniqueFuelTypes);
    }

    @Override
    public String toString() {
        return "print_unique_fuel_type: display unique values of the fuelType field for all elements in the collection";
    }
}
