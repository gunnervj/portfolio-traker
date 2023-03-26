package com.tickertraker.portfoliotraker.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenamePortfolioRequest {
    @NotBlank(message="Portfolio Name cannot be empty")
    @Size(message = "Portfolio Name must be between 2 and 25 characters", min = 2, max = 25)
    private String portfolioName;
}
