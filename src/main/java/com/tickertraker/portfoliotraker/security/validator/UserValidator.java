package com.tickertraker.portfoliotraker.security.validator;

import com.tickertraker.portfoliotraker.security.entity.User;
import com.tickertraker.portfoliotraker.security.exception.UnauthorizedException;
import com.tickertraker.portfoliotraker.security.jwt.JwtProvider;
import com.tickertraker.portfoliotraker.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserValidator {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public Boolean isTokenUserValid(String token) {
       String subject = jwtProvider.getTokenSubject(token);
       boolean status = this.checkUser(subject);
       log.info("User status {}", status);
       return status;
    }

    private boolean checkUser(String userName) {
        log.info("Username from token:: {}", userName);
        boolean status = false;
        try {
            log.info("Initializing db check for user");
           User user = userRepository.findByUserName(userName).log()
                    .toFuture().get();
            status = null == user ? false :true;
        } catch (Exception ex) {
            log.error("Error getting user from db: {}", ex.getMessage());
        }
        return status;
    }
}
