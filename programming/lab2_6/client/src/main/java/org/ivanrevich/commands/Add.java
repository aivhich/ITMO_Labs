package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда добавления нового транспортного средства в коллекцию.
 * <p>
 * Создаёт новый объект {@link Vehicle}
 * и добавляет его в коллекцию
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
     * @param args аргументы команды
     * @return {@link ResultCode#SUCCESS} после успешного добавления
     */
    @Override
    public ResultCode run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);
        Vehicle vehicle = (new VehicleFactory(ioManager)).createVehicle();
        try {
            Response<?> r = client.sendObject(new Request<Vehicle>(CommandType.ADD, vehicle));
            return r.getResultCode();
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        }
    }
}
