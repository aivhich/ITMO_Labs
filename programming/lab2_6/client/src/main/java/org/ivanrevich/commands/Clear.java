package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда очистки коллекции.
 * <p>
 * Удаляет все элементы из коллекции транспортных средств.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class Clear implements Command{
    private final ManagersLocator managersLocator;

    @Override
    public ResultCode run(String[] args) {
        Client client = managersLocator.get(Client.class);
        try {
            Response<?> r = client.sendObject(new Request<>(CommandType.CLEAR, null));
            return r.getResultCode();
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        } catch (ClassNotFoundException e)
        {
            return ResultCode.INVALID_REQUEST;
        }
    }

    @Override
    public String toString() {
        return "clear: clear collection";
    }

    public Clear(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }
}
