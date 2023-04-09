package com.tickertraker.portfoliotraker.core.constants;

import com.tickertraker.portfoliotraker.exception.PortfolioTrakerException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Tickers {
    Apple("AAPL", "Apple", 100.0),
    IBM("IBM", "IBM", 98.2),
    TESLA("TSLA", "Tesla", 52.5),
    HomeDepot("HD", "Home Depot", 22.4);

    private String ticker;
    private String description;
    private Double basePrice;

    public static Tickers getTicker(String ticker) {
        return Arrays.stream(Tickers.values())
                .filter(tick -> tick.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .orElseThrow(() -> new PortfolioTrakerException("Invalid Ticker.", HttpStatus.BAD_REQUEST));
    }
}
