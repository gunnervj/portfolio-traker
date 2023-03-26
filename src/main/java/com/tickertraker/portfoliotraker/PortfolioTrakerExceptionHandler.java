package com.tickertraker.portfoliotraker;

import com.tickertraker.portfoliotraker.core.dto.ErrorDTO;
import com.tickertraker.portfoliotraker.exception.PortfolioTrakerException;
import com.tickertraker.portfoliotraker.exception.UnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PortfolioTrakerExceptionHandler {
    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<ErrorDTO> handleUnknownException(UnknownException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(PortfolioTrakerException.class)
    public ResponseEntity<ErrorDTO> handlePortfolioTrakerException(PortfolioTrakerException ex){
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new ErrorDTO(ex.getMessage()));
    }



}
