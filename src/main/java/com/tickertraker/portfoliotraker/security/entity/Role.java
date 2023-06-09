package com.tickertraker.portfoliotraker.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Role {
    private String name;
    private String description;
}
