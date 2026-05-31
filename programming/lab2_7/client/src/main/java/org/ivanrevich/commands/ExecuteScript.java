package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.FileIOManagerImpl;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.utils.ResultCode;

/**
 * Команда выполнения скрипта из файла.
 * <p>
 * Считывает команды из указанного файла и исполняет их последовательно.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class ExecuteScript implements Command{
    private final ManagersLocator locator;

    public ExecuteScript(ManagersLocator locator) {
        this.locator = locator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManagerStack stack = locator.get(IOManagerStack.class);

        if(args.length !=1) throw new AppException(ResultCode.INVALID_NUM_OF_ARGS);


        IOManager fileIO = new FileIOManagerImpl(args[0]);
        stack.push(fileIO);
        locator.register(IOManager.class, fileIO);

        return ResultCode.SUCCESS;

    }

    @Override
    public String toString() {
        return "execute_script file_name: Read and execute the script from the specified file. The script contains commands in the same form in which the user enters them in interactive mode.";
    }
}
