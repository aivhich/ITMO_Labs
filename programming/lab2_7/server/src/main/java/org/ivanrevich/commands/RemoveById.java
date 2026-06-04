package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.Objects;

/**
 * Команда удаления элемента по идентификатору.
 * <p>
 * Удаляет транспортное средство из коллекции по его уникальному ID.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class RemoveById implements Command{
    private ManagersLocator managersLocator;

    public RemoveById(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        UserManager userManager = managersLocator.get(UserManager.class);

        try {
            int id = Integer.parseInt(String.valueOf(r.getArgs()));
            if(!Objects.equals(queueManager.getOwnerById(id), userManager.getIdForUser(r.getCredentials()))) {
                return new Result<>(ResultCode.HAVENT_OWNER_RULES,
                        "Exception while update vehicle. You haven't owner rules",
                        "Exception while update vehicle. You haven't owner rules");
            }
            queueManager.remove_by_id(id);

            return new Result<>(ResultCode.SUCCESS, "Success", id);
        } catch (AppException e) {
            return new Result<>(ResultCode.ID_ISN_EXIST, "Fail", e.getMessage());
        }
    }

    @Override
    public Result<?> run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        if(args.length!=1) throw new AppException(ResultCode.INVALID_NUM_OF_ARGS);
        try {
            queueManager.remove_by_id(Integer.parseInt(args[0]));
        } catch (AppException e) {
            return new Result<>(ResultCode.ID_ISN_EXIST, "Fail", e.getMessage());
        }
        return new Result<>(ResultCode.SUCCESS, "Success", "");
    }

    @Override
    public String toString() {
        return "remove_by_id id: remove an element from a collection by its id";
    }
}
