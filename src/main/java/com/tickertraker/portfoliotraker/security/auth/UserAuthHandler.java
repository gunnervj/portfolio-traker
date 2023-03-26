package com.tickertraker.portfoliotraker.security.auth;

import com.tickertraker.portfoliotraker.core.dto.ErrorDTO;
import com.tickertraker.portfoliotraker.security.dto.*;
import com.tickertraker.portfoliotraker.security.entity.Role;
import com.tickertraker.portfoliotraker.security.entity.User;
import com.tickertraker.portfoliotraker.security.exception.InvalidRequest;
import com.tickertraker.portfoliotraker.security.exception.UnauthorizedException;
import com.tickertraker.portfoliotraker.security.jwt.JwtProvider;
import com.tickertraker.portfoliotraker.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthHandler {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(this::login)
                .flatMap(data -> ServerResponse.ok().bodyValue(data))
                .onErrorResume(error -> {
                    if (error instanceof UnauthorizedException) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).bodyValue(new ErrorDTO(error.getMessage()));
                    } else {
                        log.error("Error Occurred ::{}", error.getMessage());
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDTO("Something Went Wrong Ty Again."));
                    }
                });
    }


    public Mono<ServerResponse> signup(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignupRequest.class)
                .flatMap(this::signup)
                .flatMap(data -> ServerResponse.ok().bodyValue(data))
                .onErrorResume(error -> {
                    if (error instanceof InvalidRequest) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorDTO(error.getMessage()));
                    } else {
                        log.error("Error Occurred ::{}", error.getMessage());
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorDTO("Something Went Wrong Ty Again."));
                    }
                });
    }

    private Mono<SignupResponse> signup(SignupRequest request) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleDTO.USER.getRoleName(), RoleDTO.USER.getDescription()));
        User user = User.builder().userName(request.getUserName()).name(request.getName())
                .password(passwordEncoder.encode(request.getPassword())).roles(roles).build();
        return userRepository.existsUserByUserName(request.getUserName())
                .flatMap(exists -> exists ? Mono.error(new InvalidRequest("Cannot signup with this username")) : this.save(user));
    }


    private Mono<SignupResponse> save(User user) {
        return userRepository.save(user)
                .flatMap(signedupUser -> Mono.just(SignupResponse.builder().userName(signedupUser.getUserName()).build()));
    }

    private Mono<LoginResponse> login(LoginRequest request) {
        return userRepository.findByUserName(request.getUserName())
                        .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                        .map(user -> new LoginResponse(jwtProvider.createToken(user)))
                        .switchIfEmpty(Mono.error( new UnauthorizedException("Invalid Login")));
    }

}
