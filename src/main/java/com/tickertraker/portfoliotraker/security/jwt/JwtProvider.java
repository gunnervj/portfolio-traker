package com.tickertraker.portfoliotraker.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private static final String ROLES = "roles";
    private static final String NAME = "name";
    private static final String USER_ID = "userId";
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;

    @PostConstruct
    public void init() {
        Assert.notNull(jwtProperties.getSecretKey(),
                "ERROR Initializing JWTProvider - Secret Key Is Null.");
        algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.getIssuer())
                .build();
    }


    public String createToken(com.tickertraker.portfoliotraker.security.entity.User user) {
        String userName = user.getUserName();
        String delimitedRoles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(","));
        Instant expiresAt = Instant.now().plusMillis(jwtProperties.getValidityInMs());
        return JWT.create().withIssuer(jwtProperties.getIssuer())
                .withSubject(userName)
                .withClaim(ROLES, delimitedRoles)
                .withClaim(NAME, user.getName())
                .withClaim(USER_ID, user.getId())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(decodedJWT.getClaim(ROLES).toString());
        User principal = new User(decodedJWT.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        boolean isValid = false;
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            if (decodedJWT.getExpiresAtAsInstant().isAfter(Instant.now()) &&
                    StringUtils.hasText(decodedJWT.getIssuer()) &&
                    decodedJWT.getIssuer().equalsIgnoreCase(jwtProperties.getIssuer())) {
                isValid = true;
            }
        } catch (JWTVerificationException ex) {
            log.error("Invalid token :: {}", ex.getMessage());
        }

        return isValid;
    }

    public String getTokenSubject(String token) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException ex) {
            log.error("Invalid token :: {}", ex.getMessage());
        }
        return "";
    }

}
