package com.tickertraker.portfoliotraker.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Holding {
    @EqualsAndHashCode.Include
    private String ticker;
    private String description;
    private Integer quantity;
    private Double price;
    private Double unrealizedGainLoss;

    public void recalculateGainLoss(double currentPrice) {
        unrealizedGainLoss = (currentPrice * quantity) - (price * quantity);
    }

}
