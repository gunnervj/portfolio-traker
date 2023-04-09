package com.tickertraker.portfoliotraker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationProperties
public class PortfolioTrakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioTrakerApplication.class, args);
	}

}
