package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;

public interface EntityFullNameService {
    String getBookFullName(Book book);

    String getCommentFullText(Commentary commentary);
}
