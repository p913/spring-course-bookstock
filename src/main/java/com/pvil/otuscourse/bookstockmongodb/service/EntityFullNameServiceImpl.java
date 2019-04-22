package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class EntityFullNameServiceImpl implements EntityFullNameService {
    private SimpleDateFormat fmtDateTime = new SimpleDateFormat("HH:mm dd.MM.yy");

    @Override
    public String getBookFullName(Book book) {
        StringBuilder res = new StringBuilder();
        res.append(book.getAuthor().getName())
                .append(", \"").append(book.getTitle()).append("\"")
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
                .append(" at ").append(fmtDateTime.format(commentary.getDate()))
                .append(" wrote: ")
                .append(commentary.getText())
                .append(" (Id=").append(commentary.getId()).append(")");
        return res.toString();
    }
}
