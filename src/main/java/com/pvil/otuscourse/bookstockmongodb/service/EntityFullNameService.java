package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;

public interface EntityFullNameService {
    String getBookFullName(Book book);

    String getCommentFullText(Commentary commentary);
}
