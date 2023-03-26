package com.tickertraker.portfoliotraker.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public class UserAuthRouterConfig {
    private final UserAuthHandler userAuthHandler;

    @Bean
    public RouterFunction authRoute() {
        return RouterFunctions.route(POST("/auth/login").and(accept(MediaType.APPLICATION_JSON)), userAuthHandler::login)
                .andRoute(POST("/auth/signup").and(accept(MediaType.APPLICATION_JSON)), userAuthHandler::signup);
    }


}
