package com.pvil.otuscourse.batchpgsql2mongo.service;

public interface Source2TargetItemConvertService {
    com.pvil.otuscourse.batchpgsql2mongo.domain.target.User
            convertUser(com.pvil.otuscourse.batchpgsql2mongo.domain.source.User user);

    com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author
            convertAuthor(com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author author);

    com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book
        convertBookWithComments(com.pvil.otuscourse.batchpgsql2mongo.domain.source.Book book);

}
