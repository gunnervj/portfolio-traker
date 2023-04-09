package com.tickertraker.portfoliotraker.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "current_ticker_price")
@ToString
public class CurrentTickerPrice {
    @Id
    private String ticker;
    private Double currentPrice;
    private String description;
    private LocalDateTime lastUpdatedDateTime;
}
