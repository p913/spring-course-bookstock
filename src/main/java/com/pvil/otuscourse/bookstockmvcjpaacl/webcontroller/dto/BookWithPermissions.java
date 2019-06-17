package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookWithPermissions {
    private Book book;

    private List<UserWithPermissions> userWithPermissionsList =
            new ArrayList<>();

    public BookWithPermissions(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<UserWithPermissions> getUserWithPermissionsList() {
        return userWithPermissionsList;
    }

    public void setUserWithPermissionsList(List<UserWithPermissions> userWithPermissionsList) {
        this.userWithPermissionsList = userWithPermissionsList;
    }
}
