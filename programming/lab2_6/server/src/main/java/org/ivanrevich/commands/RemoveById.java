package org.ivanrevich.commands;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.responses.Result;

/**
 * Команда удаления элемента по идентификатору.
 * <p>
 * Удаляет транспортное средство из коллекции по его уникальному ID.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class RemoveById implements Command{
    private ManagersLocator managersLocator;

    public RemoveById(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        try {
            int id = Integer.parseInt(String.valueOf(r.getArgs()));
            queueManager.remove_by_id(id);

            return new Result<>(ResultCode.SUCCESS, "Success", id);
        } catch (NumberFormatException e) {
            return new Result<>(ResultCode.INVALID_ARGS, "Fail", "Invalid arguments apply to command.");
        }
    }

    @Override
    public String toString() {
        return "remove_by_id id: remove an element from a collection by its id";
    }
}
