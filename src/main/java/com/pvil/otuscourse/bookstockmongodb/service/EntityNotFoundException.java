package com.pvil.otuscourse.bookstockmongodb.service;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
