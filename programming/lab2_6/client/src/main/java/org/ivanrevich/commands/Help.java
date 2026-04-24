package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

/**
 * Команда вывода справки по доступным командам.
 * <p>
 * Выводит описание всех зарегистрированных команд в приложении.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandManager
 */
public class Help implements Command{

    private final ManagersLocator managersLocator;

    public Help(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);
        //CommandManager commandManager = managersLocator.get(CommandManager.class);

        try {
            Response<?> r = client.sendObject(new Request<>(CommandType.HELP, null));
            ioManager.write((String) r.getBody());
            return r.getResultCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "help: display help on available commands";
    }
}
