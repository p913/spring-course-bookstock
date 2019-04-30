package com.pvil.otuscourse.bookstockmvcclassic.service;

public class EntityHasReferences extends RuntimeException {
    public EntityHasReferences(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EntityHasReferences(String msg) {
        super(msg);
    }

    public EntityHasReferences(Throwable cause) {
        super(cause);
    }

}
