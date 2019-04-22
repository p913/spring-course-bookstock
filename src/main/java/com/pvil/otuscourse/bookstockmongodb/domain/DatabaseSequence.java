package com.pvil.otuscourse.bookstockmongodb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("dbsequences")
public class DatabaseSequence {
    @Id
    private String id;

    @Field("seq")
    private long sequence;

    public DatabaseSequence(String id, long sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    public DatabaseSequence() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
