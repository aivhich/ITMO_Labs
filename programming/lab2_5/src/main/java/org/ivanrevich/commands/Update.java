package org.ivanrevich.commands;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;


/**
 * Команда обновления элемента по идентификатору.
 * <p>
 * Обновляет существующее транспортное средство по ID с вводом новых данных.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 * @see VehicleFactory
 */
public class Update implements Command{
    private final ManagersLocator managersLocator;

    public Update(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result run(String[] args) {
        if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);

        QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);

        try {
            int id = Integer.parseInt(args[0]);

            if(!queueManager.isExistWithId(id)) {
                throw new RuntimeException(Exceptions.ID_ISN_EXIST);
            }

            Vehicle old = queueManager.getById(id);
            ioManager.write(String.format("--- Updating element with id=%s ---", args[0]));
            ioManager.write(old.toString());
            queueManager.updateById(id,  (new VehicleFactory(ioManager)).updateVehicle(old));

            ioManager.write("Successfully updated vehicle name: " + old.getId());
            return Result.SUCCESS;
        } catch (NumberFormatException e) {
            throw new RuntimeException(Exceptions.INVALID_ARGS);
        }
    }

    @Override
    public String toString() {
        return "update id {element} : update the value of the collection element whose id is equal to the given one";
    }
}
