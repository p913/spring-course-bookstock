package com.pvil.otuscourse.bookstockmvcjpaacl.service;

public interface UserPermissionsForBookService {
    void grantDefaults(long bookId);

    void set(long bookId, String principal, Boolean grantRead, Boolean grantWrite, Boolean grantDelete);

    void set(String principal, Boolean grantCreate);

    boolean isGrantedReadAndComment(long bookId, String principal);

    boolean isGrantedWrite(long bookId, String principal);

    boolean isGrantedDelete(long bookId, String principal);

    boolean isGrantedCreateNew(String principal);
}
