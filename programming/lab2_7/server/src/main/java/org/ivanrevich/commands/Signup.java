package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.User;
import org.ivanrevich.auth.UserDTO;
import org.ivanrevich.managers.UserManager;
import org.ivanrevich.network.ResponseHandler;
import org.ivanrevich.network.Server;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Signup implements Command{
    private final Logger logger = Logger.getLogger(Server.class.getName());

    private ManagersLocator managersLocator;

    public Signup(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public String toString() {
        return "signup [username] [password]: create new user";
    }

    @Override
    public Result<?> run(Request<?> r) {
        logger.log(Level.INFO, "start signup ");
        UserManager userManager = managersLocator.get(UserManager.class);
        /// CREATE USER
        Credentials credentials = (Credentials) r.getArgs();
        logger.log(Level.INFO, "start signup "+credentials.getUsername());
        User user = userManager.signup(credentials);
        if(user!=null) {
            logger.log(Level.INFO, "Зарегистрирован новый пользователь " + user.getUsername());
            return new Result<>(ResultCode.SUCCESS,
                    "Successfully created new user",
                    new UserDTO(user.getId(), user.getUsername()));
        }else{
            return new Result<>(ResultCode.INVALID_USERNAME,
                    "Try to user other username",null);
        }
    }

    @Override
    public Result<?> run(String[] args) {
        return new Result<>(ResultCode.INVALID_REQUEST, "You already authorized as admin and can't create users", "You already authorized as admin and can't create users");
    }
}
