package com.tickertraker.portfoliotraker.security;

import com.tickertraker.portfoliotraker.security.jwt.JwtProvider;
import com.tickertraker.portfoliotraker.security.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtProvider jwtProvider;
    private final UserValidator userValidator;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        return Mono.just(jwtProvider.validateToken(authToken))
                .filter(valid -> valid)
                .map(valid -> userValidator.isTokenUserValid(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    return jwtProvider.getAuthentication(authToken);
                });
    }
}
