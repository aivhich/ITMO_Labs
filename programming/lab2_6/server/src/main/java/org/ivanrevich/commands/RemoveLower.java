package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

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

        Vehicle reference;
        try {
            reference = (Vehicle) r.getArgs();
        } catch (ClassCastException e) {
            return new Result<>(ResultCode.INVALID_INPUT, "Fail", "Некорректный аргумент команды.");
        }

        if (reference == null) {
            return new Result<>(ResultCode.INVALID_INPUT, "Fail", "Эталонный объект не передан.");
        }

        // Используем removeIf со Stream API вместо ручного итератора
        long removedCount = queueManager.getAll().stream()
                .filter(v -> v.compareTo(reference) < 0)
                .count();

        queueManager.getAll().removeIf(v -> v.compareTo(reference) < 0);

        return new Result<>(ResultCode.SUCCESS, "Success", (int) removedCount);
    }

    @Override
    public String toString() {
        return "remove_lower {element} : Remove all elements from the collection that are less than the given element";
    }
}