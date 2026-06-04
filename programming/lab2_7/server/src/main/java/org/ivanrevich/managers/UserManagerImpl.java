package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.User;
import org.ivanrevich.persistence.EntityManager;

import javax.sql.DataSource;
import java.util.HashSet;

public class UserManagerImpl implements UserManager{
    private final EntityManager entityManager;

    public UserManagerImpl(DataSource dataSource) {
        this.entityManager = new EntityManager(dataSource);
        this.entityManager.register(User.class);
    }

    @Override
    public User signup(Credentials credentials) {
        if(entityManager.findByField(User.class, "username", credentials.getUsername()).isEmpty()){
            User user = new User(credentials.getUsername(), credentials.getPassword());
            return user;
        };
        return null;
    }

    @Override
    public void changePassword(Credentials old, String newPassword) {
        User user = entityManager.findByField(User.class, "username", old.getUsername()).orElse(null);
        if(user!=null){
            if(user.isPasswordEqual(old.getPassword())){
                user.updatePassword(newPassword);
                entityManager.update(user);
            }
        }
    }

    @Override
    public User verify(Credentials credentials) {
        User user = entityManager.findByField(User.class, "username", credentials.getUsername()).orElse(null);
        if(user!=null) {
            if(user.isPasswordEqual(credentials.getPassword())){
                return user;
            }
        }
        return null;
    }

    @Override
    public Integer getIdForUser(Credentials credentials) {
        return verify(credentials).getId();
    }
}
