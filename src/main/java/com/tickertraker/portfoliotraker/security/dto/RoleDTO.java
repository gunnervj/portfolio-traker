package com.tickertraker.portfoliotraker.security.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleDTO {
    ADMIN("Administrator", "Administrator"),
    USER("User", "Regular User");

    private String roleName;
    private String description;
}
