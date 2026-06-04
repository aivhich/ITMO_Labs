package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;
/**
 * Команда очистки коллекции.
 * <p>
 * Удаляет все элементы из коллекции транспортных средств.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see QueueManager
 */
public class Clear implements Command{
    private final ManagersLocator managersLocator;

    @Override
    public Result<?> run(Request<?> r) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        UserManager userManager = managersLocator.get(UserManager.class);
        queueManager.clear(userManager.getIdForUser(r.getCredentials()));
        return new Result<>(ResultCode.SUCCESS, "Success", "Successfully clear collection");
    }

    @Override
    public Result<?> run(String[] args) {
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        queueManager.clear();
        return new Result<>(ResultCode.SUCCESS, "Success", "Successfully clear collection");
    }

    @Override
    public String toString() {
        return "clear: clear collection";
    }

    public Clear(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }
}
