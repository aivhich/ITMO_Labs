package org.ivanrevich.commands;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.StorageManager;

import java.util.ArrayList;

import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

/**
 * Команда сохранения коллекции в файл.
 * <p>
 * Сохраняет текущее состояние коллекции в CSV-файл через {@link StorageManager}.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see StorageManager
 * @see QueueManager
 */
public class Save implements Command{
    private final ManagersLocator managersLocator;
    private String path;

    public Save(ManagersLocator managersLocator, String path) {
        this.path = path;
        this.managersLocator = managersLocator;
    }

    @Override
    public Result<?> run(Request<?> r) {
        StorageManager storageManager = managersLocator.get(StorageManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);
//        IOManager ioManager = managersLocator.get(IOManager.class);
        //TODO WATCH DOCS
        //if(args.length==1) path=args[0];

        storageManager.save(new ArrayList<>(queueManager.getAll()), path);
//        ioManager.write("Saved "+path);

        return new Result(ResultCode.SUCCESS, "Success", path);
    }

    @Override
    public String toString() {
        return "save : save collection to file";
    }
}