package com.pvil.otuscourse.bookstockmvcclassic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("authors")
public class Author {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        this(null, name);
    }

    public Author() {
    }

    public Author(Author author) {
        this.id = author.id;
        this.name = author.name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return Objects.equals(id, author.id) &&
                Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AUTHOR{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
