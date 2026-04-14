package org.ivanrevich.commands;

import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;


/**
 * Команда добавления нового транспортного средства в коллекцию.
 * <p>
 * Создаёт новый объект {@link Vehicle} через {@link VehicleFactory}
 * и добавляет его в коллекцию через {@link QueueManager}.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 * @see VehicleFactory
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
     * @param args аргументы команды (не используются)
     * @return {@link Result#SUCCESS} после успешного добавления
     */
    @Override
    public Result run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);

        Vehicle vehicle = (new VehicleFactory(ioManager)).createVehicle();
        queueManager.add(vehicle);

        ioManager.write("Successfully created vehicle");
        return Result.SUCCESS;
    }
}
