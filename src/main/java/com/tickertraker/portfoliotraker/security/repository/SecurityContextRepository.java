package com.tickertraker.portfoliotraker.security.repository;

import com.tickertraker.portfoliotraker.security.AuthenticationManager;
import com.tickertraker.portfoliotraker.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authorizationHeader -> authorizationHeader.startsWith(TOKEN_PREFIX))
                .flatMap(authorizationHeader -> {
                    String authorizationToken = authorizationHeader.replace(TOKEN_PREFIX, "");
                    Authentication authentication = new UsernamePasswordAuthenticationToken(authorizationToken,
                            authorizationToken);
                    return this.authenticationManager.authenticate(authentication).map(SecurityContextImpl::new);
                });
    }
}
