package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.Objects;

/**
 * Команда удаления и вывода первого элемента коллекции.
 * <p>
 * Извлекает первый элемент из приоритетной очереди и выводит его.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class RemoveHead implements Command{
    private final ManagersLocator managersLocator;

    public RemoveHead(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "remove_head: print the first element of the collection and remove it";
    }

    @Override
    public Result<?> run(Request<?> request) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        UserManager userManager = managersLocator.get(UserManager.class);
        if(queueManager.getLast()==null){
            return new Result<>(ResultCode.SUCCESS, "Success", null);
        }
        if(!Objects.equals(
                queueManager.getOwnerById(queueManager.getLast().getAuthorId()),
                userManager.getIdForUser(request.getCredentials())
        )) {
            return new Result<>(ResultCode.HAVENT_OWNER_RULES,
                    "Exception while update vehicle. You haven't owner rules",
                    "Exception while update vehicle. You haven't owner rules");
        }
        Vehicle vehicle = queueManager.remove_head();
        return new Result<>(ResultCode.SUCCESS, "Success", vehicle);
    }

    @Override
    public Result<?> run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);
        Vehicle v = queueManager.remove_head();
        if(v!=null){
            ioManager.write(v.toString());
        }else{
            ioManager.write("Collections is empty, there are nothing to remove");
        }
        return new Result<>(ResultCode.SUCCESS, "Success", queueManager.remove_head());
    }
}
