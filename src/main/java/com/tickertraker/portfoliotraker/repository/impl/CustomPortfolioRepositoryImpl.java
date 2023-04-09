package com.tickertraker.portfoliotraker.repository.impl;

import com.mongodb.BasicDBObject;
import com.tickertraker.portfoliotraker.core.constants.CustomConstants;
import com.tickertraker.portfoliotraker.repository.CustomPortfolioRepository;
import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomPortfolioRepositoryImpl implements CustomPortfolioRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Portfolio> addNewHoldingToPortfolio(String portfolioName, String owner, Holding holding) {
        Query query = new Query(Criteria.where(CustomConstants.PORTFOLIO_NAME).is(portfolioName.trim())
                .and(CustomConstants.OWNER).is(owner));
        Update update = new Update().set(CustomConstants.LAST_UPDATED_DATETIME, LocalDateTime.now())
                .addToSet(CustomConstants.HOLDINGS, holding);
        log.info("Saving to DB: PortfolioName: {} | Owner: {} | Holding: {}",
                portfolioName, owner, holding);
        return mongoTemplate.findAndModify(query, update, Portfolio.class).log();
    }

    @Override
    public Mono<Portfolio> updateHoldingInPortfolio(String portfolioName, String owner, Holding holding) {
        this.removeHoldingInPortfolio(portfolioName, owner, holding.getTicker());
        return this.addNewHoldingToPortfolio(portfolioName, owner, holding);
    }

    @Override
    public Mono<Portfolio> removeHoldingInPortfolio(String portfolioName, String owner, String ticker) {
        Query query = new Query(Criteria.where(CustomConstants.PORTFOLIO_NAME).is(portfolioName)
                .and(CustomConstants.OWNER).is(owner));

        Update update = new Update().set(CustomConstants.LAST_UPDATED_DATETIME, LocalDateTime.now())
                .pull(CustomConstants.HOLDINGS, new BasicDBObject(CustomConstants.TICKER, ticker));
        return mongoTemplate.findAndModify(query, update, Portfolio.class);
    }

    @Override
    public Mono<Portfolio> savePortfolioUnrealizedGainLoss(String portfolioName, String owner, Double unrealizedGainLoss) {
        Query query = new Query(Criteria.where(CustomConstants.PORTFOLIO_NAME).is(portfolioName)
                .and(CustomConstants.OWNER).is(owner));
        Update update = new Update().set(CustomConstants.UNREALIZED_GAIN_LOSS, unrealizedGainLoss)
                .set(CustomConstants.LAST_UPDATED_DATETIME, LocalDateTime.now());
        return mongoTemplate.findAndModify(query, update, Portfolio.class);
    }
}
