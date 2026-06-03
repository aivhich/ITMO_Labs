package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.models.User;

public interface UserManager {
    User signup(Credentials credentials);
    //void login(Credentials credentials);
    //void logout(Credentials credentials);
    void changePassword(Credentials old, Credentials newCredentials);
    User verify(Credentials credentials);
}
