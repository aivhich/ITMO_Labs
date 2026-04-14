package org.ivanrevich.commands;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;

/**
 * Команда удаления и вывода первого элемента коллекции.
 * <p>
 * Извлекает первый элемент из приоритетной очереди и выводит его.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class RemoveHead implements Command{
    private ManagersLocator managersLocator;

    public RemoveHead(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "remove_head: print the first element of the collection and remove it";
    }

    @Override
    public Result run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);
        ioManager.write(queueManager.remove_head().toString());
        return Result.SUCCESS;
    }
}
