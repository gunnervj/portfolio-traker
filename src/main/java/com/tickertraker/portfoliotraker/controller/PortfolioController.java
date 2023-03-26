package com.tickertraker.portfoliotraker.controller;

import com.tickertraker.portfoliotraker.controller.dto.CreatePortfolioRequest;
import com.tickertraker.portfoliotraker.controller.dto.CreatePortfolioResponse;
import com.tickertraker.portfoliotraker.controller.dto.RenamePortfolioRequest;
import com.tickertraker.portfoliotraker.controller.dto.RenamePortfolioResponse;
import com.tickertraker.portfoliotraker.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

@RequestMapping("/users/_current/portfolio")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public Mono<ResponseEntity<CreatePortfolioResponse>> create(@RequestBody @Valid Mono<CreatePortfolioRequest> request,
                                                                Principal principal) {
        return portfolioService.createPortfolio(request, principal)
                .map(portfolio ->  ResponseEntity.created(URI.create("/" + portfolio.getPortfolioName())).body(portfolio));
    }

    @DeleteMapping("/{portfolioName}")
    public Mono<Void> delete(@PathVariable String portfolioName, Principal principal) {
        return portfolioService.deletePortfolio(portfolioName, principal);
    }

    @PutMapping("/{portfolioName}")
    public Mono<RenamePortfolioResponse> rename(@PathVariable String portfolioName,
                                                @RequestBody @Valid Mono<RenamePortfolioRequest> request,
                                                Principal principal) {
        return portfolioService.renamePortfolio(request, portfolioName, principal);
    }

}
