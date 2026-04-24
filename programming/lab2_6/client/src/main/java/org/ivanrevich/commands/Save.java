package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayList;

/**
 * Команда сохранения коллекции в файл.
 * <p>
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 */
public class Save implements Command{
    private final ManagersLocator managersLocator;
    private String path;

    public Save(ManagersLocator managersLocator, String path) {
        this.path = path;
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
//        IOManager ioManager = managersLocator.get(IOManager.class);
//        if(args.length==1) path=args[0];
//
//        storageManager.save(new ArrayList<>(queueManager.getAll()), path);
////        ioManager.write("Saved "+path);

        return ResultCode.SUCCESS;
    }

    @Override
    public String toString() {
        return "save : save collection to file";
    }
}