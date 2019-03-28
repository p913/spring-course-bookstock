package com.pvil.otuscourse.bookstockjdbc.domain;

public interface NamedEntityWithId {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);
}
