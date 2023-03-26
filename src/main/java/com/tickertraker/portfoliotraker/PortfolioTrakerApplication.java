package com.tickertraker.portfoliotraker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@ConfigurationProperties
public class PortfolioTrakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioTrakerApplication.class, args);
	}

}
