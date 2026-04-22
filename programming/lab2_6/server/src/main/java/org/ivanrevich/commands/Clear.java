package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;

/**
 * Команда очистки коллекции.
 * <p>
 * Удаляет все элементы из коллекции транспортных средств.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class Clear implements Command{
    private final ManagersLocator managersLocator;

    @Override
    public Result run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        queueManager.clear();
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "clear: clear collection";
    }

    public Clear(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }
}
