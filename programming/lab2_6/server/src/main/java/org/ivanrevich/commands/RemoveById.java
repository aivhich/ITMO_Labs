package org.ivanrevich.commands;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;

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
    public Result run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);
        try {
            queueManager.remove_by_id(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            throw new RuntimeException(Exceptions.INVALID_ARGS);
        }
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "remove_by_id id: remove an element from a collection by its id";
    }
}
