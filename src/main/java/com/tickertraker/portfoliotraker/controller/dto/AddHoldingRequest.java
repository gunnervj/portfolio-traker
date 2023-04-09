package com.tickertraker.portfoliotraker.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddHoldingRequest {
    @NotBlank(message="ticker cannot be empty")
    @Size(message = "ticket must be between 2 and 4 characters", min = 2, max = 4)
    private String ticker;
    @PositiveOrZero(message = "quantity should be 0 or greater than 0")
    @Max(value = 100000, message = "Maximum quantity allowed to hold is 100000")
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;
    @PositiveOrZero(message = "Invested Price should be 0 or greater than 0")
    @NotNull(message = "Invested Price is mandatory")
    private Double invPrice;
}
