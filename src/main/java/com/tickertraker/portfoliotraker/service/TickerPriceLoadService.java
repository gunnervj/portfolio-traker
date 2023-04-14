package com.tickertraker.portfoliotraker.service;

import com.tickertraker.portfoliotraker.core.constants.ErrorMessages;
import com.tickertraker.portfoliotraker.core.constants.Tickers;
import com.tickertraker.portfoliotraker.exception.UnknownException;
import com.tickertraker.portfoliotraker.repository.CurrentTickerPriceRepository;
import com.tickertraker.portfoliotraker.repository.entity.CurrentTickerPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Component
public class TickerPriceLoadService {

    private final CurrentTickerPriceRepository currentTickerPriceRepository;
    private final Random random = new Random();

    @Scheduled(fixedRate = 60000, initialDelay=10000)
    public void loadPrices() {
        log.info("Refreshing price");
        Tickers ticker = getRandomTicker();
        log.info("Random Ticker is " + ticker.getTicker());
        currentTickerPriceRepository.findById(ticker.getTicker())
                .log()
                .switchIfEmpty(Mono.defer(() ->this.createCurrentPriceEntryWithBasePrice(ticker)))
                .map(this::calculateAndSetNewPrice)
                .onErrorResume(error -> {
                   log.error("ERROR While saving current Price"  + error.getMessage());
                   return Mono.empty();
                })
                .flatMap(currentTickerPrice -> {
                    return this.currentTickerPriceRepository.save(currentTickerPrice);
                })
                .map(currentTickerPrice -> {
                    log.info("Saved for {}", currentTickerPrice);
                    return Mono.just(currentTickerPrice);
                }).subscribe();
    }


    private Mono<CurrentTickerPrice> createCurrentPriceEntryWithBasePrice(Tickers tickers) {
        CurrentTickerPrice currentTickerPrice = new CurrentTickerPrice();
        currentTickerPrice.setTicker(tickers.getTicker());
        currentTickerPrice.setCurrentPrice(tickers.getBasePrice());
        currentTickerPrice.setLastUpdatedDateTime(LocalDateTime.now());
        currentTickerPrice.setDescription(tickers.getDescription());
        log.info("Created Object with base price::" + currentTickerPrice.getTicker());
        return Mono.justOrEmpty( currentTickerPrice);
    }

    private CurrentTickerPrice calculateAndSetNewPrice(CurrentTickerPrice currentTickerPrice) {
        log.info("Setting new Ticker price as " + currentTickerPrice.getCurrentPrice());
        currentTickerPrice.setCurrentPrice(currentTickerPrice.getCurrentPrice() + (currentTickerPrice.getCurrentPrice() * getRandomPercentage() / 100));
        log.info("Setting new Ticker price as " + currentTickerPrice.getCurrentPrice());
        return currentTickerPrice;
    }

    private Tickers getRandomTicker() {
        int position = random.nextInt(Tickers.values().length);
        return Tickers.values()[position];
    }

    private Double getRandomPercentage() {
        return random.nextDouble(-1, 1);
    }
}
