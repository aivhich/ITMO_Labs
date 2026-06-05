package org.ivanrevich.auth;

import org.ivanrevich.annotations.*;

import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name="user_")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name = "username")
    @Unique
    private String username;
    @Column(name = "password")
    private byte[] password;
    @Column(name="salt")
    private String salt;

    public void setPassword(String password) {
        this.password = password.getBytes();
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.salt = SaltGenerator.generateSalt();
        this.password = PasswordService.getHash(password, salt);
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

    public byte[] getPassword() {
        return password;
    }

    public boolean isPasswordEqual(String outerPassword) {
        return (Arrays.equals(PasswordService.getHash(outerPassword, salt), password));
    }

    public void updatePassword(String newPassword) {
        this.salt = SaltGenerator.generateSalt();
        this.password = PasswordService.getHash(newPassword, salt);
    }
}
