package com.tickertraker.portfoliotraker.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@RequiredArgsConstructor
public class UserDetailsRouterConfig {
    private final UserDetailsHandler userDetailsHandler;

    @Bean
    public RouterFunction userRoute() {
        return RouterFunctions.route(GET("/users/_current").and(accept(MediaType.APPLICATION_JSON)), userDetailsHandler::getUserDetails);
    }
}
