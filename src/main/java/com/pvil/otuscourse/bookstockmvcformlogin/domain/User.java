package com.pvil.otuscourse.bookstockmvcformlogin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Document("users")
public class User {
    @Id
    private String id;

    @Size(min=4, max=30)
    private String login;

    @Size(min=6, max=30)
    private String password;

    @NotNull
    private String fullName;

    @NotNull
    private String email;

    private boolean stockKeeper;

    public User(String id, String login, String password, String fullName, String email, boolean isStockKeeper) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.stockKeeper = isStockKeeper;
    }

    public User(String login, String password, String fullName, String email, boolean isStockKeeper) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.stockKeeper = isStockKeeper;
    }

    public User(User user) {
        this.id = user.id;
        this.login = user.login;
        this.password = user.password;
        this.fullName = user.fullName;
        this.email = user.email;
        this.stockKeeper = user.stockKeeper;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStockKeeper() {
        return stockKeeper;
    }

    public void setStockKeeper(boolean stockKeeper) {
        this.stockKeeper = stockKeeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return stockKeeper == user.stockKeeper &&
                Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", stockKeeper=" + stockKeeper +
                '}';
    }
}
