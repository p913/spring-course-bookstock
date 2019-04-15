package com.pvil.otuscourse.bookstockdatajpa.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private long id;

    @Column(name = "genre_name")
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(long id) {
        this.id = id;
    }

    public Genre(String name) {
        this(0, name);
    }

    public Genre() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id &&
                Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "GENRE{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
