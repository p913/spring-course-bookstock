package com.pvil.otuscourse.bookstockjpajpql.service;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;

public interface EntityFullNameService {
    String getBookFullName(Book book);

    String getCommentFullText(Commentary commentary);
}
