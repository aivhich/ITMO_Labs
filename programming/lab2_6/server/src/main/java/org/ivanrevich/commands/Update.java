package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.validators.VehicleValidate;

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
 */
public class Update implements Command{
    private final ManagersLocator managersLocator;

    public Update(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {

        QueueManager queueManager = managersLocator.get(QueueManager.class);
        try {
            Vehicle newv = (Vehicle) r.getArgs();
            if (!(new VehicleValidate()).apply(newv))
                return new Result<Vehicle>(ResultCode.INVALID_INPUT, "Exception while update vehicle. Invalid input", newv);

            if(!queueManager.isExistWithId(newv.getId())) {
                return new Result<>(ResultCode.ID_ISN_EXIST, "Fail", "Entity with that id is not exists.");
            }
            queueManager.updateById(newv.getId(),  newv);

            //ioManager.write("Successfully updated vehicle name: " + old.getId());
            return new Result<>(ResultCode.SUCCESS, "Success", "Successfully updated vehicle "+newv.getId());
        } catch (NumberFormatException e) {
            return new Result<>(ResultCode.INVALID_ARGS, "Fail", "Invalid arguments apply to command.");
        }
    }

    @Override
    public String toString() {
        return "update id {element} : update the value of the collection element whose id is equal to the given one";
    }
}
