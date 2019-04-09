package com.pvil.otuscourse.bookstockjpajpql.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private long id;

    @Column(name = "book_name")
    private String name = "";

    @Column(name = "isbn")
    private String isbn = "";

    @Column(name = "publisher")
    private String publisher = "";

    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author = new Author("");

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre = new Genre("");

    public Book(long id, String name, String isbn, String publisher, int year, Author author, Genre genre) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public Book(String name, String isbn, String publisher, int year, Author author, Genre genre) {
        this(0, name, isbn, publisher, year, author, genre);
    }

    public Book(long id) {
        this.id = id;
    }

    public Book() {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                year == book.year &&
                Objects.equals(name, book.name) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(author, book.author) &&
                Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", author=" + author +
                ", genre=" + genre +
                '}';
    }
}

