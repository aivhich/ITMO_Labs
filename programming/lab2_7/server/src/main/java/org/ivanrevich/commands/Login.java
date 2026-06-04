package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.User;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.network.Server;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Login implements Command{
    private final Logger logger = Logger.getLogger(Server.class.getName());

    private ManagersLocator managersLocator;

    public Login(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "login [username] [password]: log in as someone";
    }

    @Override
    public Result<?> run(Request<?> r) {
        UserManager userManager = managersLocator.get(UserManager.class);
        User user = userManager.verify(r.getCredentials());
        if(user!=null){
            return new Result<>(ResultCode.SUCCESS,
                    "Successfully logged user "+r.getCredentials().getUsername(),
                    user.getId());
        }

        return new Result<>(ResultCode.INVALID_INPUT,
                "Successfully logged user "+r.getCredentials().getUsername(),
                null);
    }

    @Override
    public Result<?> run(String[] args) {
        return new Result<>(ResultCode.INVALID_REQUEST, "You already authorized as admin and can't create users", "You already authorized as admin and can't create users");
    }
}
