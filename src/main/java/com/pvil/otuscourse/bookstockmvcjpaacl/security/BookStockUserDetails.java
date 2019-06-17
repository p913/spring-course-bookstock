package com.pvil.otuscourse.bookstockmvcjpaacl.security;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockPredefinedAuthorities.ADMIN_USER_LOGIN;

public class BookStockUserDetails implements UserDetails {
    private final User user;

    public BookStockUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();

        roles.add(new SimpleGrantedAuthority(BookStockPredefinedAuthorities.ROLE_READER));
        if (user.isStockKeeper())
            roles.add(new SimpleGrantedAuthority(BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER));
        if (ADMIN_USER_LOGIN.equals(user.getLogin()))
            roles.add(new SimpleGrantedAuthority(BookStockPredefinedAuthorities.ROLE_ADMIN));

        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
