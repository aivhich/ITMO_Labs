package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда удаления элемента по идентификатору.
 * <p>
 * Удаляет транспортное средство из коллекции по его уникальному ID.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class RemoveById implements Command {
    private final ManagersLocator managersLocator;

    public RemoveById(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        if (args.length != 1) return ResultCode.INVALID_NUM_OF_ARGS;
        Client client = managersLocator.get(Client.class);
        try {
            int id = Integer.parseInt(args[0]);
            Response<?> r = client.sendObject(new Request<>(CommandType.REMOVE_BY_ID, id));
            return r.getResultCode();
        } catch (NumberFormatException e) {
            return ResultCode.INVALID_ARGS;
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        }
    }

    @Override
    public String toString() {
        return "remove_by_id id: remove an element from a collection by its id";
    }
}