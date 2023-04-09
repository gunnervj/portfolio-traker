package com.tickertraker.portfoliotraker.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHoldingRequest {
    @PositiveOrZero(message = "quantity should be 0 or greater than 0")
    @Max(value = 100000, message = "Maximum quantity allowed to hold is 100000")
    @NotNull(message = "quantity is mandatory")
    private Integer quantity;
    @PositiveOrZero(message = "price should be 0 or greater than 0")
    private Double invPrice;
}
