package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.utils.ResultCode;

public class Logout implements Command{
    private final ManagersLocator managersLocator;

    public Logout(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "logout - you now";
    }

    @Override
    public ResultCode run(String[] args) {
        AuthManager authManager = managersLocator.get(AuthManager.class);

        boolean isSuccessfullyLoggedIn = authManager.logout();
        if(isSuccessfullyLoggedIn){
            return ResultCode.SUCCESS;
        }
        return ResultCode.INVALID_REQUEST;//TODO
    }
}
