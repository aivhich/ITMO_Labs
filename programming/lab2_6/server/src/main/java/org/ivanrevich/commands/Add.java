package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.responses.Result;

/**
 * Команда добавления нового транспортного средства в коллекцию.
 * <p>
 * Создаёт новый объект {@link Vehicle}
 * и добавляет его в коллекцию через {@link QueueManager}.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class Add implements Command {
    /** Локатор менеджеров для получения зависимостей */
    private final ManagersLocator managersLocator;

    /**
     * Конструктор команды Add.
     *
     * @param managersLocator локатор для получения менеджеров
     */
    public Add(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "add {element}: add new element to collection";
    }

    /**
     * Выполняет команду добавления транспортного средства.
     * <p>
     * Запрашивает данные у пользователя, создаёт объект Vehicle
     * и добавляет его в коллекцию.
     * </p>
     *
     * @param r аргументы команды
     * @return {@link ResultCode#SUCCESS} после успешного добавления
     */
    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        //IOManager ioManager = managersLocator.get(IOManager.class);
        //Vehicle vehicle = (new VehicleFactory(ioManager)).createVehicle();
        Vehicle vehicle = (Vehicle) r.getArgs();
        queueManager.add(vehicle);
        ///ioManager.write("Successfully created vehicle"); @DEPRECATED
        return new Result<Vehicle>(ResultCode.SUCCESS, "Successfully created vehicle", vehicle);
    }
}
