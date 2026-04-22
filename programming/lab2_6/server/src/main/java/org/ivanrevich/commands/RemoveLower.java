package org.ivanrevich.commands;

import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;

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
 * @see VehicleFactory
 */
public class RemoveLower implements Command{
    private final  ManagersLocator managersLocator;

    public RemoveLower(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        io.write("Enter the reference vehicle to remove all lower elements:");
        VehicleFactory factory = new VehicleFactory(io);
        Vehicle reference;

        try {
            reference = factory.createVehicleForRef();
        } catch (IllegalArgumentException e) {
            io.write("Invalid input, operation aborted.");
            return Result.FAIL;
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

        io.write("Removed " + removedCount + " vehicles that were lower than the reference.");
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "remove_lower {element} : Remove all elements from the collection that are less than the given element";
    }
}
