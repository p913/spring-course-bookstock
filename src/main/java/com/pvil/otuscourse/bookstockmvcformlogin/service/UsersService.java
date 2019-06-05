package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;

import java.util.Optional;

public interface UsersService {
    Optional<User> getById(String id);

    Optional<User> getByLogin(String login);

    boolean existsByLogin(String login);

    void register(User user);
}
