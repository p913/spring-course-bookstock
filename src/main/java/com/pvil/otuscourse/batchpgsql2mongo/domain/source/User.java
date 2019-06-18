package com.pvil.otuscourse.batchpgsql2mongo.domain.source;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String login;

    private String password;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    @Column(name = "is_stock_keeper")
    private boolean stockKeeper;

    public User(long id, String login, String password, String fullName, String email, boolean isStockKeeper) {
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

    public User(long id) {
        this.id = id;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
