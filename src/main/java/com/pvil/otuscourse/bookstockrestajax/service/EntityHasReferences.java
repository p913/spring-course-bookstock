package com.pvil.otuscourse.bookstockrestajax.service;

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
