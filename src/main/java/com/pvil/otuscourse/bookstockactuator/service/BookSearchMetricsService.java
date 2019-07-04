package com.pvil.otuscourse.bookstockactuator.service;

public interface BookSearchMetricsService {
    double getAvgOfSearchResults();

    void countSearchCase(long countOfBooks);

    void alive();

    boolean isAlive();
}
