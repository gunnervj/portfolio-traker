package com.tickertraker.portfoliotraker.repository;

import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PortfolioRepository extends ReactiveMongoRepository<Portfolio, String> {
    Mono<Boolean> existsPortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
    Mono<Portfolio> findPortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
    Mono<Void> deletePortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
}
