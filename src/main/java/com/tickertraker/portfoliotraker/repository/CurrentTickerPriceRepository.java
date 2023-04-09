package com.tickertraker.portfoliotraker.repository;

import com.tickertraker.portfoliotraker.repository.entity.CurrentTickerPrice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CurrentTickerPriceRepository extends ReactiveMongoRepository<CurrentTickerPrice, String> {

    Flux<CurrentTickerPrice> findByTickerIn(List<String> tickers);

}
