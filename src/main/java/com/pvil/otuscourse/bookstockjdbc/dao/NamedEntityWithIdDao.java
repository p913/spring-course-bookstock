package com.pvil.otuscourse.bookstockjdbc.dao;

import java.util.List;
import java.util.Optional;

public interface NamedEntityWithIdDao<T> {
    List<T> getAll();

    Optional<T> getById(int id);

    List<T> getByName(String filter);

    int add(T author);

    boolean delete(T author);

    boolean update(T author);
}
