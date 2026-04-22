package org.ivanrevich.commands;

import org.ivanrevich.managers.ManagersLocator;


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
    public Result run(String[] args) {
        System.exit(0);
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "exit: terminate the program (without saving to file)";
    }
}
