package com.tickertraker.portfoliotraker.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class CreatePortfolioResponse {
    private String portfolioName;
    private String createdDate;
}
