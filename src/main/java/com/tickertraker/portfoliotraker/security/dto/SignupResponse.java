package com.tickertraker.portfoliotraker.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponse {
    private String userName;
}
