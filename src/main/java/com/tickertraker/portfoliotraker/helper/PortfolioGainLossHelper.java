package com.tickertraker.portfoliotraker.helper;

import com.tickertraker.portfoliotraker.repository.CurrentTickerPriceRepository;
import com.tickertraker.portfoliotraker.repository.entity.CurrentTickerPrice;
import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioGainLossHelper {
    private final CurrentTickerPriceRepository currentTickerPriceRepository;
    public Mono<Portfolio> calculateGainLoss(Portfolio portfolio) {
        List<String> holdingTickers =
            portfolio.getHoldings()
                    .stream()
                    .map(Holding::getTicker)
                    .collect(Collectors.toList());
            log.info("Holding Tickers ::{}", holdingTickers);

        return currentTickerPriceRepository.findByTickerIn(holdingTickers)
                .onErrorResume(error -> {
                    log.error("Error while getting current ticker prices for [{}] |  Error:: {}",
                            holdingTickers, error.getMessage());
                    return Mono.empty();
                })
                .switchIfEmpty(Flux.defer(() ->this.logEmpty()))
                .collectList()
                .map(currentTickerPrices ->
                        currentTickerPrices.stream()
                                .collect(Collectors.toMap(CurrentTickerPrice::getTicker, CurrentTickerPrice::getCurrentPrice)))
                .map(currentPriceMap -> this.reCalculateGainLoss(currentPriceMap, portfolio));
    }

    private Portfolio reCalculateGainLoss(Map<String, Double> currentPriceMap, Portfolio portfolio) {
        portfolio.getHoldings()
                .stream()
                .forEach(holdingDto -> holdingDto.recalculateGainLoss(currentPriceMap.get(holdingDto.getTicker())));
        portfolio.reCalculateUnrealizedGainLoss();
        return portfolio;
    }

    private Flux<CurrentTickerPrice> logEmpty() {
        log.info("NO result found when getting current Ticker Prices");
        return Flux.empty();
    }


}
