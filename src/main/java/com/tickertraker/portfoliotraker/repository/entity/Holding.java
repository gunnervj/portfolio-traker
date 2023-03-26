package com.tickertraker.portfoliotraker.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Holding {
    private String ticker;
    private String title;
    private String description;
    private String quantity;
    private String price;
    private Double unrealizedGainLoss;
}
