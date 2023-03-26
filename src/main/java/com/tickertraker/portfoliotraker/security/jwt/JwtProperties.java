package com.tickertraker.portfoliotraker.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private long validityInMs = 3600000;
    private String issuer = "TickerTraker";
}
