package com.tickertraker.portfoliotraker.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoldingDto {
    private String ticker;
    private String description;
    private Integer quantity;
    private Double invPrice;
    private Double unrealizedGainLoss;

}
