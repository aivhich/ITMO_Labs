package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
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
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        String dateStr;
        try{
            dateStr = queueManager.getAll().stream()
                    .min(Comparator.comparing(Vehicle::getCreationDate))
                    .orElseThrow().getCreationDate()
                    .toString();
        } catch (Exception e){
            dateStr = "Collection is empty, no creation date available.";
        }

        return new Result<>(ResultCode.SUCCESS, "Success",
                Map.of(
                        "type", queueManager.getAll().getClass().getSimpleName(),
                        "count", String.valueOf(queueManager.size()),
                        "init_date", dateStr
                )
        );
    }

    @Override
    public Result<?> run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);

        io.write("Collection type: " + queueManager.getAll().getClass().getSimpleName());
        io.write("Number of elements: " + queueManager.size());

        try{
            String dateStr = queueManager.getAll().stream()
                    .min(Comparator.comparing(Vehicle::getCreationDate))
                    .orElseThrow().getCreationDate()
                    .toString();
            io.write("Date of first element: " + dateStr);
            return new Result<>(ResultCode.SUCCESS, "Success",
                    Map.of(
                            "type", queueManager.getAll().getClass().getSimpleName(),
                            "count", String.valueOf(queueManager.size()),
                            "init_date", dateStr
                    )
            );
        } catch (Exception e){
            io.write("Collection is empty, no creation date available.");
            return new Result<>(ResultCode.SUCCESS, "Success",
                    Map.of(
                            "type", queueManager.getAll().getClass().getSimpleName(),
                            "count", String.valueOf(queueManager.size()),
                            "init_date", ""
                    )
            );
        }

    }

    @Override
    public String toString() {
        return "info: print information about the collection (type, initialization date, number of elements, etc.) to the standard output stream.";
    }
}
