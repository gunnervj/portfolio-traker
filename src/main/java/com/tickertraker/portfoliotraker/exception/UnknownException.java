package com.tickertraker.portfoliotraker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnknownException extends RuntimeException{
    private String message;
}
