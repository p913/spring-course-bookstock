package com.pvil.otuscourse.bookstockmongodb.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

public class Commentary {
    @Transient
    public static final String SEQUENCE_NAME = "comments_sequence";

    private long id;

    private String text;

    @Field("post_date")
    private Date date;

    @Field("reader_name")
    private String reader;

    public Commentary(long id, String text, Date date, String reader) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.reader = reader;
    }

    public Commentary(String text, Date date, String reader) {
        this.text = text;
        this.date = date;
        this.reader = reader;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commentary that = (Commentary) o;
        return id == that.id &&
                Objects.equals(text, that.text) &&
                Objects.equals(date, that.date) &&
                Objects.equals(reader, that.reader);
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
                '}';
    }
}
