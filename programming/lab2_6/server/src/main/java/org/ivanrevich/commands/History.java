package org.ivanrevich.commands;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.requests.Request;
import org.ivanrevich.utils.CommandObj;
import org.ivanrevich.utils.ResultCode;
import org.ivanrevich.responses.Result;
import java.util.ArrayList;
import java.util.List;


/**
 * Команда вывода истории выполненных команд.
 * <p>
 * Выводит последние N команд (по умолчанию 13) без их аргументов.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandObj
 * @see CommandManager
 */
public class History implements Command{
    private final ManagersLocator managersLocator;

    public History(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "history: print the last 13 commands (without their arguments)";
    }

    @Override
    public Result<List<CommandObj>> run(Request<?> r) {
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        ArrayList<CommandObj> fullHistory = commandManager.getHistory();

        int i = 13;
        int startIdx = fullHistory.size()-(i);
        if(startIdx<0) startIdx=0;

        return new Result<>(ResultCode.SUCCESS, "Success", new ArrayList<>(fullHistory.subList(startIdx, fullHistory.size())));
    }

    @Override
    public Result<?> run(String[] args) {
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);
        ArrayList<CommandObj> fullHistory = commandManager.getHistory();

        int i = 13;
        if(args.length>=1) {
            try{
                i = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new AppException(ResultCode.INVALID_ARGS);
            }
        }
        if(args.length>=2) throw new AppException(ResultCode.INVALID_NUM_OF_ARGS);

        int startIdx = fullHistory.size()-(i);
        if(startIdx<0) startIdx=0;

        for (CommandObj commandObj : fullHistory.subList(startIdx, fullHistory.size())) {
            ioManager.write(commandObj.name());
        }
        return new Result<>(ResultCode.SUCCESS, "Success", new ArrayList<>(fullHistory.subList(startIdx, fullHistory.size())));
    }
}
