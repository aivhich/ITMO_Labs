package org.ivanrevich.models;


import org.ivanrevich.annotations.*;

import java.io.Serializable;

@Entity
@Table(name="user_")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Unique
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
