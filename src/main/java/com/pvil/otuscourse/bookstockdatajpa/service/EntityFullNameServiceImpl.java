package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EntityFullNameServiceImpl implements EntityFullNameService {
    private DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    @Override
    public String getBookFullName(Book book) {
        StringBuilder res = new StringBuilder();
        res.append(book.getAuthor().getName())
                .append(", \"").append(book.getName()).append("\"")
                .append(", ISBN ").append(book.getIsbn())
                .append(", ").append(book.getPublisher())
                .append(", ").append(book.getYear())
                .append(" (Id=").append(book.getId()).append(")");
        return res.toString();
    }

    @Override
    public String getCommentFullText(Commentary commentary) {
        StringBuilder res = new StringBuilder();
        res.append(commentary.getReader())
                .append(" at ").append(commentary.getDate().format(fmtDateTime))
                .append(" wrote: ")
                .append(commentary.getText())
                .append(" (Id=").append(commentary.getId()).append(")");
        return res.toString();
    }
}
