package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.utils.ResultCode;

public class Login implements Command{
    private final ManagersLocator managersLocator;

    public Login(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "login [username] [password]: send request to check and set your credentials";
    }

    @Override
    public ResultCode run(String[] args) {
        AuthManager authManager = managersLocator.get(AuthManager.class);

        if(args.length != 2){
            return ResultCode.INVALID_REQUEST;
        }
        boolean isSuccessfullyLoggedIn = authManager.authenticate(args[0], args[1]);
        if(isSuccessfullyLoggedIn){
            return ResultCode.SUCCESS;
        }
        return ResultCode.INVALID_REQUEST;//TODO
    }
}
