package commands;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.User;
import org.ivanrevich.managers.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimal in-memory UserManager test double.
 * Maps username -> userId. verify() succeeds only if the
 * Credentials' password equals "correct-password".
 */
public class StubUserManager implements UserManager {
    private final Map<String, Integer> usernameToId = new HashMap<>();
    private int nextId = 1;

    public int registerUser(String username) {
        int id = nextId++;
        usernameToId.put(username, id);
        return id;
    }

    @Override
    public User signup(Credentials credentials) {
        if (usernameToId.containsKey(credentials.getUsername())) return null;
        registerUser(credentials.getUsername());
        User u = new User();
        u.setUsername(credentials.getUsername());
        u.setId(usernameToId.get(credentials.getUsername()));
        return u;
    }

    @Override
    public void changePassword(Credentials old, String newPassword) {
        // not needed for tests
    }

    @Override
    public User verify(Credentials credentials) {
        if (credentials == null) return null;
        Integer id = usernameToId.get(credentials.getUsername());
        if (id == null) return null;
        if (!"correct-password".equals(credentials.getPassword())) return null;
        User u = new User();
        u.setUsername(credentials.getUsername());
        u.setId(id);
        return u;
    }

    @Override
    public Integer getIdForUser(Credentials credentials) {
        if (credentials == null) return null;
        return usernameToId.get(credentials.getUsername());
    }
}
