package com.pvil.otuscourse.bookstockjpajpql.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private long id;

    @Column(name = "author_name")
    private String name;

    public Author(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        this(0, name);
    }

    public Author(long id) {
        this.id = id;
    }

    public Author() {
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
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "AUTHOR{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
