package com.tickertraker.portfoliotraker.repository;

import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import reactor.core.publisher.Mono;

public interface CustomPortfolioRepository {
    Mono<Portfolio> addNewHoldingToPortfolio(String portfolioName, String owner, Holding holding);
    Mono<Portfolio> updateHoldingInPortfolio(String portfolioName, String owner, Holding holding);
    Mono<Portfolio> removeHoldingInPortfolio(String portfolioName, String owner, String ticker);
    Mono<Portfolio> savePortfolioUnrealizedGainLoss(String portfolioName, String owner, Double unrealizedGainLoss);
}
