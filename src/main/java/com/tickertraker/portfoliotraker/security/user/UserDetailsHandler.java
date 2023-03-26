package com.tickertraker.portfoliotraker.security.user;

import com.tickertraker.portfoliotraker.core.dto.ErrorDTO;
import com.tickertraker.portfoliotraker.security.dto.UserDetails;
import com.tickertraker.portfoliotraker.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserDetailsHandler {
    private final UserRepository userRepository;

    public Mono<ServerResponse> getUserDetails(ServerRequest serverRequest) {
        return serverRequest.principal().flatMap(principal -> userRepository.findByUserName(principal.getName()))
                .flatMap(user -> ServerResponse.ok()
                        .bodyValue(UserDetails.builder().userName(user.getUserName()).name(user.getName()).build()))
                .onErrorResume(error -> {
                    log.error("Error while getting user details : Error:", error.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDTO("Something went wrong."));
                });
    }
}
