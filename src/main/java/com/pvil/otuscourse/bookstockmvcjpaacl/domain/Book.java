package com.pvil.otuscourse.bookstockmvcjpaacl.domain;

import com.pvil.otuscourse.bookstockmvcjpaacl.validation.IsbnConstraint;

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
    private String title = "";

    @IsbnConstraint
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

    public Book(long id, String title, String isbn, String publisher, int year, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, String isbn, String publisher, int year, Author author, Genre genre) {
        this(0, title, isbn, publisher, year, author, genre);
    }

    public Book(long id) {
        this.id = id;
    }

    public Book() {
    }

    public Book(Book book) {
        this.id = book.id;
        this.title = book.title;
        this.isbn = book.isbn;
        this.publisher = book.publisher;
        this.year = book.year;
        this.author = book.author;
        this.genre = book.genre;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                Objects.equals(title, book.title) &&
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
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", author=" + author +
                ", genre=" + genre +
                '}';
    }
}

