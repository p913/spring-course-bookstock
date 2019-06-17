package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void addCommentary(Commentary commentary);

    boolean deleteCommentary(Commentary commentary);
}
