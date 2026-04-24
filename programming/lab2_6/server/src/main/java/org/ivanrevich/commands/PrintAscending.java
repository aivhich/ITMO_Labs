package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ivanrevich.responses.Result;
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
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
//        IOManager ioManager = managersLocator.get(IOManager.class);
        List<Vehicle> vehicleList = new ArrayList<>(queueManager.getAll());
        Collections.sort(vehicleList);
//        vehicleList.forEach(vehicle -> ioManager.write(vehicle.toString()));
        return new Result<>(ResultCode.SUCCESS, "Success", vehicleList);
    }

    @Override
    public String toString() {
        return "print_ascending: display collection elements in ascending order";
    }
}
