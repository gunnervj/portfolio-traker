package com.tickertraker.portfoliotraker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class PortfolioTrakerException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
}
