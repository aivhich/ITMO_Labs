package org.ivanrevich.managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.UserDTO;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;

public class AuthManagerImpl implements AuthManager{
    private Credentials credentials;
    private Integer userId = null;
    private final ManagersLocator managersLocator;

    public AuthManagerImpl(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public boolean authenticate(String username, String password) {
        Client client = managersLocator.get(Client.class);
        Credentials rCred = new Credentials(username, password);
        try {
            Response<Integer> response = (Response<Integer>) client.sendObject(new Request<>(CommandType.LOGIN, rCred));
            System.out.println(response.getMessage());
            if(response.getResultCode() == ResultCode.SUCCESS) {
                credentials = rCred;
                userId = response.getBody();
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    @Override
    public Integer authorizedUserId() {
        return userId;
    }

    @Override
    public boolean register(String username, String password) {
        Client client = managersLocator.get(Client.class);
        Credentials rCred = new Credentials(username, password);
        try {
            Response<?> response = (Response<?>) client.sendObject(new Request<>(CommandType.SIGNUP, rCred)); /// sigup data in body orin cred??? TODO DEBUG HERE
            if(response.getResultCode() == ResultCode.SUCCESS && response.getBody() instanceof UserDTO) {
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean logout() {
        credentials = null;
        userId = null;
        return true;
    }
}
