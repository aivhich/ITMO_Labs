package org.ivanrevich.commands;

import org.ivanrevich.responses.Result;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;

import java.util.Comparator;
import java.util.Map;


/**
 * Команда вывода информации о коллекции.
 * <p>
 * Выводит тип коллекции, количество элементов и дату создания первого элемента.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class Info implements Command{
    private final ManagersLocator managersLocator;

    public Info(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public org.ivanrevich.responses.Result<?> run(Request<?> request) {
        //IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        //io.write("Collection type: " + queueManager.getAll().getClass().getSimpleName());
        //io.write("Number of elements: " + queueManager.size());
        String dateStr;
        try{
            dateStr = queueManager.getAll().stream()
                    .min(Comparator.comparing(Vehicle::getCreationDate))
                    .orElseThrow().getCreationDate()
                    .toString();
        } catch (Exception e){
            dateStr="Collection is empty, no creation date available.";
        }

        return new Result<>(ResultCode.SUCCESS, "Success", Map.of("type", queueManager.getAll().getClass().getSimpleName(),
                "numberOfElements", String.valueOf(queueManager.size()),
                "initDate", dateStr));
    }

    @Override
    public String toString() {
        return "info: print information about the collection (type, initialization date, number of elements, etc.) to the standard output stream.";
    }
}
