package com.tickertraker.portfoliotraker.repository;

import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import com.tickertraker.portfoliotraker.repository.projections.PortfolioMetaData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioRepository extends ReactiveMongoRepository<Portfolio, String>, CustomPortfolioRepository {
    Mono<Boolean> existsPortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
    Mono<Portfolio> findPortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
    Flux<PortfolioMetaData> findPortfolioByOwner(String owner);
    Mono<Void> deletePortfolioByPortfolioNameAndOwner(String portfolioName, String owner);
}
