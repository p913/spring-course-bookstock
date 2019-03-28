package com.pvil.otuscourse.bookstockjdbc.domain;

import java.util.Objects;

public class Book implements NamedEntityWithId {
    private int id;

    private String name = "";

    private String isbn = "";

    private String publisher = "";

    private int year;

    private Author author = new Author("");

    private Genre genre = new Genre("");

    public Book(int id, String name, String isbn, String publisher, int year, Author author, Genre genre) {
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

    public Book() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
        return id;
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

