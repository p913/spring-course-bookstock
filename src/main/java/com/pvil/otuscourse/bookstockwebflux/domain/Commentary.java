package com.pvil.otuscourse.bookstockwebflux.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

public class Commentary {
    private String text;

    private String id;

    @Field("post_date")
    private Date date;

    @Field("reader_name")
    private String reader;

    public Commentary(String text, Date date, String reader) {
        this.text = text;
        this.date = date;
        this.reader = reader;
    }

    public Commentary(String id, String text, Date date, String reader) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.reader = reader;
    }

    public Commentary(String id) {
        this.id = id;
    }

    public Commentary() {
    }

    public Commentary(Commentary commentary) {
        this.id = commentary.id;
        this.text = commentary.text;
        this.date = commentary.date;
        this.reader = commentary.reader;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return Objects.equals(text, that.text) &&
                Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(reader, that.reader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Commentary{" +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", reader='" + reader + '\'' +
                '}';
    }
}
