package org.ivanrevich.network;

import org.ivanrevich.managers.*;
import org.ivanrevich.requests.Request;

public class CommandHandler {
    private ManagersLocator managersLocator;


    public CommandHandler(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    public void run(Request r){
        CommandManager commandManager = managersLocator.get(CommandManager.class);

        commandManager.run(r.getCommandType().getName());
    }
}
