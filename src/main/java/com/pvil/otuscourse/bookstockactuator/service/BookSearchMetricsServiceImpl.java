package com.pvil.otuscourse.bookstockactuator.service;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Service;

@Service
public class BookSearchMetricsServiceImpl implements BookSearchMetricsService {
    private static final long ALIVE_INTERVAL_SEC = 3600;

    /**
     * Для вычисления среднего кол-ва книг в результатах поиска - всего книг
     */
    private volatile long totalBookInSearchResults;

    /**
     * Для вычисления среднего кол-ва книг в результатах поиска - кол-во запросов
     */
    private volatile long searchCount;

    /**
     * Искусственный параметр "здоровья" приложения - кто-то смотрел книги в течение последнего часа
     */
    private volatile long aliveTimestamp = 0;

    private final Counter counterOfSearchHits;

    private final Counter counterOfSearchMisses;

    public BookSearchMetricsServiceImpl(Counter counterOfSearchHits, Counter counterOfSearchMisses) {
        this.counterOfSearchHits = counterOfSearchHits;
        this.counterOfSearchMisses = counterOfSearchMisses;
    }

    @Override
    public double getAvgOfSearchResults() {
        synchronized (this) {
            if (searchCount > 0)
                return (double) totalBookInSearchResults / searchCount;
        }
        return 0;
    }

    @Override
    public void countSearchCase(long countOfBooks) {
        if (countOfBooks > 0) {
            counterOfSearchHits.increment();

            synchronized (this) {
                searchCount++;
                totalBookInSearchResults += countOfBooks;
            }
        }
        else
            counterOfSearchMisses.increment();
    }

    @Override
    public void alive() {
        aliveTimestamp = System.currentTimeMillis();
    }

    @Override
    public boolean isAlive() {
        return System.currentTimeMillis() - aliveTimestamp < ALIVE_INTERVAL_SEC * 1000;
    }


}
