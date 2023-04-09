package com.tickertraker.portfoliotraker.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioMetaDataDto {
    private String portfolioName;
    private String createdDate;
    private String lastUpdatedDate;
}
