package com.tickertraker.portfoliotraker.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SignupRequest {
    private String userName;
    private String name;
    private String password;
    private List<RoleDTO> roleDTOS;
}
