package com.tickertraker.portfoliotraker.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetails {
    private String userName;
    private String name;

}
