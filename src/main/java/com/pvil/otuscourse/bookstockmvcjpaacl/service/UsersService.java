package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<User> getAll();

    List<User> getStockKeepers();

    Optional<User> getById(long id);

    Optional<User> getByLogin(String login);

    boolean existsByLogin(String login);

    void register(User user);
}
