package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.models.User;
import org.ivanrevich.persistence.EntityManager;

import javax.sql.DataSource;
import java.util.HashSet;

public class UserManagerImpl implements UserManager{
    private final EntityManager entityManager;
    private HashSet<User> users;

    public UserManagerImpl(DataSource dataSource) {
        this.entityManager = new EntityManager(dataSource);
        this.entityManager.register(User.class, Integer.class);
        this.users = (HashSet<User>) entityManager.findAll(User.class);
    }

    @Override
    public User signup(Credentials credentials) {
        User credUser = users.stream().filter(u-> u.getUsername()==credentials.getUsername()).findFirst().orElse(null);
        if(credUser==null){
            User user = entityManager.save(new User(credentials.getUsername(),credentials.getPassword()));
            users.add(user);
            return user;
        }else {
            this.users = (HashSet<User>) entityManager.findAll(User.class);
            if (entityManager.findById(User.class, credUser.getId()).isEmpty()){
                User user = entityManager.save(new User(credentials.getUsername(),credentials.getPassword()));
                users.add(user);
                return user;
            }
        }
        return null;
    }

    @Override
    public void changePassword(Credentials old, Credentials newCredentials) {

    }

    @Override
    public User verify(Credentials credentials) {
        return null;
    }
}
