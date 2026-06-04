package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;

public interface AuthManager {
    boolean authenticate(String username, String password);
    boolean register(String username, String password);
    boolean logout();
    Credentials getCredentials();
    Integer authorizedUserId();
}
