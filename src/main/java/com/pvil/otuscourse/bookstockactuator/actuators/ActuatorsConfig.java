package com.pvil.otuscourse.bookstockactuator.actuators;

import com.pvil.otuscourse.bookstockactuator.service.BookSearchMetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorsConfig {
    @Bean
    public Gauge gaugeAvgBooksInSearchResults(MeterRegistry meterRegistry, BookSearchMetricsService bookSearchMetricsService) {
        return Gauge
                .builder("app.booksearch.avg",
                        bookSearchMetricsService, BookSearchMetricsService::getAvgOfSearchResults)
                .description("Среднее количество книг в результате поиска")
                .register(meterRegistry);
    }

    @Bean
    public Counter counterOfSearchHits(MeterRegistry meterRegistry) {
        return Counter
                .builder("app.booksearch.hits")
                .description("Число успешных (с результатом) запросов на поиск книг")
                .baseUnit("Шт.")
                .register(meterRegistry);
    }

    @Bean
    public Counter counterOfSearchMisses(MeterRegistry meterRegistry) {
        return Counter
                .builder("app.booksearch.misses")
                .description("Число безуспешных запросов на поиск книг")
                .baseUnit("Шт.")
                .register(meterRegistry);
    }

    @Bean
    public HealthIndicator bookStockAppHealth(BookSearchMetricsService bookSearchMetricsService) {
        return () -> bookSearchMetricsService.isAlive()
                ? Health.up().build()
                : Health.down().build();
    }

}
