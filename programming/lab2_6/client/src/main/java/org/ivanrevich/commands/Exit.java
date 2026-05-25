package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.Serial;

/**
 * Команда завершения программы.
 * <p>
 * Завершает работу приложения без сохранения данных в файл.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class Exit implements Command{
    private final ManagersLocator managersLocator;

    public Exit(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String [] args) {
        System.exit(0);
        return ResultCode.SUCCESS;
    }

    @Override
    public String toString() {
        return "exit: terminate the program";
    }
}
