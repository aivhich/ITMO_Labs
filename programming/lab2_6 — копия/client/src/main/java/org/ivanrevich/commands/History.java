package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Команда вывода истории выполненных команд.
 * <p>
 * Выводит последние N команд (по умолчанию 13) без их аргументов.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandObj
 */
public class History implements Command{
    private final ManagersLocator managersLocator;

    public History(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "history: print the last 13 commands (without their arguments)";
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);
        ArrayList<CommandObj> fullHistory;
        try {
            fullHistory = (ArrayList<CommandObj>) client.sendObject(new Request<>(CommandType.HISTORY, null)).getBody();
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        }
        for (CommandObj commandObj : fullHistory) {
            ioManager.write(commandObj.name());
        }
        return ResultCode.SUCCESS;
    }
}
