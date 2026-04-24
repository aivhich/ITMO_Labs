package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.Iterator;

/**
 * Команда удаления элементов, меньших заданного.
 * <p>
 * Запрашивает эталонное транспортное средство и удаляет все элементы,
 * которые меньше его согласно {@link Vehicle#compareTo(Vehicle)}.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class RemoveLower implements Command{
    private final  ManagersLocator managersLocator;

    public RemoveLower(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        //IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        //io.write("Enter the reference vehicle to remove all lower elements:");
        //VehicleFactory factory = new VehicleFactory(io);
        Vehicle reference;

        try {
            //reference = factory.createVehicleForRef();
            reference = (Vehicle) r.getArgs();
        } catch (IllegalArgumentException e) {
            //io.write("Invalid input, operation aborted.");
            return new Result<>(ResultCode.FAIL_0, "Fail TODO", "Invalid input, operation aborted.");
        }

        Iterator<Vehicle> iterator = queueManager.getAll().iterator();
        int removedCount = 0;
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();
            if (v.compareTo(reference) < 0) {
                iterator.remove();
                removedCount++;
            }
        }

        //io.write("Removed " + removedCount + " vehicles that were lower than the reference.");
        return new Result(ResultCode.SUCCESS, "Success", removedCount);
    }

    @Override
    public String toString() {
        return "remove_lower {element} : Remove all elements from the collection that are less than the given element";
    }
}
