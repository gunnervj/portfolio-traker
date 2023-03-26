package com.tickertraker.portfoliotraker.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenamePortfolioResponse {
    private String portfolioName;
    private String updatedDate;
}
