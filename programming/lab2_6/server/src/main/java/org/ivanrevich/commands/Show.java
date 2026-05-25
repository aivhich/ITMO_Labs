package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда вывода всех элементов коллекции.
 * <p>
 * Возвращает клиенту список транспортных средств, отсортированный
 * по естественному порядку ({@link Vehicle#compareTo(Vehicle)}).
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 * @see QueueManager
 */
public class Show implements Command {
    private final ManagersLocator managersLocator;

    public Show(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        // Коллекция сортируется перед отправкой клиенту (требование задания)
        List<Vehicle> sorted = queueManager.getAll().stream()
                .sorted()
                .collect(Collectors.toList());

        return new Result<>(ResultCode.SUCCESS, "Success", sorted);
    }

    @Override
    public String toString() {
        return "show : Print all elements of the collection to standard output as strings.";
    }
}