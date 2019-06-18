package com.pvil.otuscourse.batchpgsql2mongo.service;

/**
 * В источнике данных (PosgreSQL) у сущностей числовые Id, в БД назначения (MongoDb) - стоковые.
 * Данный сервис обеспечивает связи старых и новых Id, чтобы меньше лазить в БД для получения новых, с кэшированием.
 */
public interface CachedIdStoreService {
    void putUserId(long sourceId, String targetId);

    String getUserId(long sourceId);

    void putAuthorId(long sourceId, String targetId);

    String getAuthorId(long sourceId);

    void reset();
}
