package com.pvil.otuscourse.bookstockjdbc.utils;

public class TooManyEntitiesException extends RuntimeException {
    public TooManyEntitiesException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TooManyEntitiesException(String msg) {
        super(msg);
    }

    public TooManyEntitiesException(Throwable cause) {
        super(cause);
    }
}
