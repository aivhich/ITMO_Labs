package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;

import java.util.PriorityQueue;

import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

/**
 * Команда вывода всех элементов коллекции.
 * <p>
 * Выводит строковое представление всех транспортных средств в коллекции.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 * @see QueueManager
 */
public class Show implements Command{
    private ManagersLocator managersLocator;

    public Show(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager =  managersLocator.get(QueueManager.class);
        PriorityQueue<Vehicle> vehicles = queueManager.getAll();
        return new Result<>(ResultCode.SUCCESS, "Success", vehicles);
    }

    @Override
    public String toString() {
        return "show : Print all elements of the collection to standard output as strings.";
    }
}
