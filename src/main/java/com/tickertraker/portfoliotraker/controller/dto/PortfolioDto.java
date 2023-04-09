package com.tickertraker.portfoliotraker.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioDto extends PortfolioMetaDataDto {
    private List<HoldingDto> holdings;
    private Double unrealizedGainLoss;

}
