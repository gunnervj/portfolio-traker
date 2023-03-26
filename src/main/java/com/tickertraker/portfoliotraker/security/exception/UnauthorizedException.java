package com.tickertraker.portfoliotraker.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException{
    private String message;
}
