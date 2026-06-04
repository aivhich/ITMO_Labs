package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда удаления элементов, меньших заданного.
 * <p>
 * Удаляет все элементы коллекции, которые меньше эталонного
 * согласно {@link Vehicle#compareTo(Vehicle)}.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class RemoveLower implements Command {
    private final ManagersLocator managersLocator;

    public RemoveLower(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        UserManager userManager = managersLocator.get(UserManager.class);

        Vehicle reference;
        try {
            reference = (Vehicle) r.getArgs();
        } catch (ClassCastException e) {
            return new Result<>(ResultCode.INVALID_INPUT, "Fail", "Некорректный аргумент команды.");
        }

        if (reference == null) {
            return new Result<>(ResultCode.INVALID_INPUT, "Fail", "Эталонный объект не передан.");
        }
        List<Vehicle> toRemove = queueManager.getAll().stream()
                .filter(v ->
                        (v.compareTo(reference) < 0)
                                && Objects.equals(v.getAuthorId(), userManager.getIdForUser(r.getCredentials())))
                .toList();

        toRemove.forEach(v -> queueManager.remove_by_id(v.getId()));
        return new Result<>(ResultCode.SUCCESS, "Success", toRemove.size());
    }

    @Override
    public Result<?> run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        io.write("Enter the reference vehicle to remove all lower elements:");
        VehicleFactory factory = new VehicleFactory(io);
        Vehicle reference;

        try {
            reference = factory.createVehicleForRef();
        } catch (IllegalArgumentException e) {
            io.write("Invalid input, operation aborted.");
            return new Result<>(ResultCode.INVALID_INPUT, "Fail", "Эталонный объект не передан.");
        }
        List<Vehicle> toRemove = queueManager.getAll().stream()
                .filter(v -> (v.compareTo(reference) < 0))
                .toList();

        toRemove.forEach(v -> queueManager.remove_by_id(v.getId()));

        io.write("Removed " + toRemove.size() + " vehicles that were lower than the reference.");
        return new Result<>(ResultCode.SUCCESS, "Success", toRemove.size());

    }

    @Override
    public String toString() {
        return "remove_lower {element} : Remove all elements from the collection that are less than the given element";
    }
}