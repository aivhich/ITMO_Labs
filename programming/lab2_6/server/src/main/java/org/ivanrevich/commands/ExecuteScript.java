package org.ivanrevich.commands;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.managers.FileIOManagerImpl;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.managers.ManagersLocator;

/**
 * Команда выполнения скрипта из файла.
 * <p>
 * Считывает команды из указанного файла и исполняет их последовательно.
 * Использует {@link IOManagerStack} для переключения источника ввода.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see FileIOManagerImpl
 * @see IOManagerStack
 */
public class ExecuteScript implements Command{
    private final ManagersLocator locator;

    public ExecuteScript(ManagersLocator locator) {
        this.locator = locator;
    }

    @Override
    public Result run(String[] args) {
        IOManagerStack stack = locator.get(IOManagerStack.class);

        if(args.length !=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);
        //TODO PATH CHECKING

        IOManager fileIO = new FileIOManagerImpl(args[0]);
        stack.push(fileIO);
        locator.register(IOManager.class, fileIO);

        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "execute_script file_name: Read and execute the script from the specified file. The script contains commands in the same form in which the user enters them in interactive mode.";
    }
}
