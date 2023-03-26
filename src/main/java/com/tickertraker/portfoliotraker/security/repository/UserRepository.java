package com.tickertraker.portfoliotraker.security.repository;

import com.tickertraker.portfoliotraker.security.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUserName(String userName);
    Mono<Boolean> existsUserByUserName(String userName);
}
