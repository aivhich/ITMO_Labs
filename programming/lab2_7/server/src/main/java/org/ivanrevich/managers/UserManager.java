package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.User;

public interface UserManager {
    User signup(Credentials credentials);
    //void login(Credentials credentials);
    //void logout(Credentials credentials);
    void changePassword(Credentials old, String newPassword);
    User verify(Credentials credentials);
}
