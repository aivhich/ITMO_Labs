package org.ivanrevich.commands;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;
import org.ivanrevich.managers.QueueManager;
import org.ivanrevich.managers.StorageManager;

import java.util.ArrayList;


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
    public Result run(String[] args) {
        StorageManager storageManager = managersLocator.get(StorageManager.class);
        QueueManager queueManager = managersLocator.get(QueueManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);

        if(args.length==1) path=args[0];

        storageManager.save(new ArrayList<>(queueManager.getAll()), path);

        ioManager.write("Saved "+path);
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "save : save collection to file";
    }
}