package com.pvil.otuscourse.batchpgsql2mongo.service;

import com.pvil.otuscourse.batchpgsql2mongo.repository.source.AuthorJpaRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.source.UserJpaRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.target.AuthorMongoRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.target.UserMongoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CachedIdStoreServiceImpl implements CachedIdStoreService {
    private final UserJpaRepository userJpaRepository;

    private final UserMongoRepository userMongoRepository;

    private final AuthorJpaRepository authorJpaRepository;

    private final AuthorMongoRepository authorMongoRepository;

    private Map<Long, String> userId = new ConcurrentHashMap<>();

    private Map<Long, String> authorId = new ConcurrentHashMap<>();

    public CachedIdStoreServiceImpl(UserJpaRepository userJpaRepository, UserMongoRepository userMongoRepository,
                                    AuthorJpaRepository authorJpaRepository, AuthorMongoRepository authorMongoRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userMongoRepository = userMongoRepository;
        this.authorJpaRepository = authorJpaRepository;
        this.authorMongoRepository = authorMongoRepository;
    }

    @Override
    public void putUserId(long sourceId, String targetId) {
        userId.put(sourceId, targetId);
    }

    @Override
    public String getUserId(long sourceId) {
        String targetId = userId.get(sourceId);

        if (targetId == null) {
            targetId = retreiveUserId(sourceId);
            putUserId(sourceId, targetId);
        }

        return targetId;
    }

    @Override
    public void putAuthorId(long sourceId, String targetId) {
        authorId.put(sourceId, targetId);
    }

    @Override
    public String getAuthorId(long sourceId) {
        String targetId = authorId.get(sourceId);

        if (targetId == null) {
            targetId = retreiveAuthorId(sourceId);
            putAuthorId(sourceId, targetId);
        }

        return targetId;
    }

    @Override
    public void reset() {
        userId.clear();
        authorId.clear();
    }

    private String retreiveUserId(long sourceId) {
        Optional<com.pvil.otuscourse.batchpgsql2mongo.domain.source.User> sourceUser =
                userJpaRepository.findById(sourceId);

        if (sourceUser.isEmpty())
            throw new EntityNotFoundException("No user with Id = " + sourceId + " in source database");

        Optional<com.pvil.otuscourse.batchpgsql2mongo.domain.target.User> targetUser =
                userMongoRepository.findByLogin(sourceUser.get().getLogin());

        if (targetUser.isEmpty())
            throw new EntityNotFoundException("No user with login = "
                    + sourceUser.get().getLogin()
                    + " in target database");

        return targetUser.get().getId();
    }

    private String retreiveAuthorId(long sourceId) {
        Optional<com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author> sourceAuthor =
                authorJpaRepository.findById(sourceId);

        if (sourceAuthor.isEmpty())
            throw new EntityNotFoundException("No author with Id = " + sourceId + " in source database");

        Optional<com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author> targetAuthor =
                authorMongoRepository.findByName(sourceAuthor.get().getName());

        if (targetAuthor.isEmpty())
            throw new EntityNotFoundException("No author with name = "
                    + sourceAuthor.get().getName()
                    + " in target database");

        return targetAuthor.get().getId();
    }

}
