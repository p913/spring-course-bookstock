package com.pvil.otuscourse.bookstockjpajpql.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "book_comments")
public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @Column(name = "comment")
    private String text;

    @Column(name = "post_date")
    private OffsetDateTime date;

    @Column(name = "reader_name")
    private String reader;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Commentary(long id, String text, OffsetDateTime date, String reader, Book book) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.reader = reader;
        this.book = book;
    }

    public Commentary(String text, OffsetDateTime date, String reader, Book book) {
        this.text = text;
        this.date = date;
        this.reader = reader;
        this.book = book;
    }

    public Commentary(long id) {
        this.id = id;
    }

    public Commentary() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commentary that = (Commentary) o;
        return id == that.id &&
                Objects.equals(text, that.text) &&
                Objects.equals(date, that.date) &&
                Objects.equals(reader, that.reader) &&
                Objects.equals(book.getId(), that.book.getId());
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "Commentary{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", reader='" + reader + '\'' +
                ", book=" + book +
                '}';
    }
}
