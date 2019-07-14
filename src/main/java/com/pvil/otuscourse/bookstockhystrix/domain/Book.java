package com.pvil.otuscourse.bookstockhystrix.domain;

import com.pvil.otuscourse.bookstockhystrix.validation.IsbnConstraint;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document("books")
public class Book {
    @Id
    private String id;

    @NotNull
    private String title = "";

    @IsbnConstraint
    private String isbn = "";

    @NotNull
    private String publisher = "";

    @Min(1800)
    @Max(2030)
    private int year;

    @NotNull
    private String genre = "";

    @DBRef
    @NotNull
    private Author author = new Author("");

    private List<Commentary> comments = new ArrayList<>();

    public Book(String id, String title, String isbn, String publisher, int year, Author author, String genre) {
        this(id, title, isbn, publisher, year, author, genre, null);
    }

    public Book(String id, String title, String isbn, String publisher, int year, Author author, String genre, List<Commentary> comments) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

    public Book(String title, String isbn, String publisher, int year, Author author, String genre) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public Book(String id) {
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
        if (book.comments != null)
            book.comments.forEach(c -> this.comments.add(new Commentary(c)));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<Commentary> getComments() {
        return comments;
    }

    public void setComments(List<Commentary> comments) {
        this.comments = comments;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", author=" + author +
                ", comments=" + comments +
                '}';
    }
}

