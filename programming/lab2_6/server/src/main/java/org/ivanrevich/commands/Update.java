package org.ivanrevich.commands;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

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
//        if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);

        QueueManager queueManager = managersLocator.get(QueueManager.class);
        //IOManager ioManager = managersLocator.get(IOManager.class);

        try {
            Vehicle newv = (Vehicle) r.getArgs();
            if(!queueManager.isExistWithId(newv.getId())) {
                throw new RuntimeException(Exceptions.ID_ISN_EXIST);
            }
            //ioManager.write(String.format("--- Updating element with id=%s ---", args[0]));
            //ioManager.write(old.toString());
            queueManager.updateById(newv.getId(),  newv);

            //ioManager.write("Successfully updated vehicle name: " + old.getId());
            return new Result<>(ResultCode.SUCCESS, "Success", "Successfully updated vehicle "+newv.getId());
        } catch (NumberFormatException e) {
            throw new RuntimeException(Exceptions.INVALID_ARGS);
        }
    }

    @Override
    public String toString() {
        return "update id {element} : update the value of the collection element whose id is equal to the given one";
    }
}
