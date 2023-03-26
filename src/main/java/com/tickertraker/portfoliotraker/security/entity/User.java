package com.tickertraker.portfoliotraker.security.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Builder
@ToString
public class User {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String userName;
    private String password;
    private List<Role> roles;
}
