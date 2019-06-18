package com.pvil.otuscourse.batchpgsql2mongo.service;

import com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author;
import com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book;
import com.pvil.otuscourse.batchpgsql2mongo.domain.target.Commentary;
import com.pvil.otuscourse.batchpgsql2mongo.domain.target.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Source2TargetItemConvertServiceImpl implements Source2TargetItemConvertService {

    private final CachedIdStoreService cachedIdStoreService;

    public Source2TargetItemConvertServiceImpl(CachedIdStoreService cachedIdStoreService) {
        this.cachedIdStoreService = cachedIdStoreService;
    }

    @Override
    public User convertUser(com.pvil.otuscourse.batchpgsql2mongo.domain.source.User user) {
        //Здесь вручную генерится id для пользователей в БД назначения, чтобы сразу же и
        //кэшировать id, и потом лишний раз не лазить в БД для связывания старого и нового id
        //пользователя
        String targetId = new ObjectId().toHexString();
        cachedIdStoreService.putUserId(user.getId(), targetId);
        return new User(targetId, user.getLogin(), user.getPassword(),
                user.getFullName(), user.getEmail(), user.isStockKeeper());
    }

    @Override
    public Author convertAuthor(com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author author) {
        String targetId = new ObjectId().toHexString();
        cachedIdStoreService.putAuthorId(author.getId(), targetId);
        return new Author(targetId, author.getName());
    }

    @Override
    public Book convertBookWithComments(com.pvil.otuscourse.batchpgsql2mongo.domain.source.Book sourceBook) {
        Book targetBook = new Book(sourceBook.getTitle(),
                sourceBook.getIsbn(),
                sourceBook.getPublisher(),
                sourceBook.getYear(),
                new Author(sourceBook.getAuthor().getName()),
                sourceBook.getGenre().getName());

        targetBook.getAuthor().setId(cachedIdStoreService.getAuthorId(sourceBook.getAuthor().getId()));

        sourceBook.getCommentaries().forEach(c -> targetBook.getComments().add(
                new Commentary(c.getText(),
                        Date.from(c.getDate().toInstant()),
                        new User(cachedIdStoreService.getUserId(c.getUser().getId()))
        )));

        return targetBook;
    }
}
