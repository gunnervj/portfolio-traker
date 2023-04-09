package com.tickertraker.portfoliotraker.controller;

import com.tickertraker.portfoliotraker.controller.dto.*;
import com.tickertraker.portfoliotraker.service.PortfolioHoldingsService;
import com.tickertraker.portfoliotraker.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

@RequestMapping("/users/_current/portfolios")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioHoldingsService portfolioHoldingsService;

    @GetMapping
    public Flux<PortfolioMetaDataDto> getAllPortfolio(Principal principal) {
        return portfolioService.getAllPortfolios(principal);
    }

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

    @GetMapping("/{portfolioName}")
    public Mono<PortfolioDto> getPortfolio(@PathVariable String portfolioName,
                                                Principal principal) {
        return portfolioService.getPortfolio(portfolioName, principal);
    }



    @PostMapping("/{portfolioName}/holdings")
    public Mono<ResponseEntity<PortfolioDto>> addHolding(@PathVariable String portfolioName,
                                                         @RequestBody @Valid Mono<AddHoldingRequest> request,
                                                         Principal principal) {
        return portfolioHoldingsService.addHolding(portfolioName, request, principal)
                .map(portfolio ->  ResponseEntity.created(URI.create("/" + portfolio.getPortfolioName()))
                        .body(portfolio));
    }

    @PutMapping("/{portfolioName}/holdings/{ticker}")
    public Mono<ResponseEntity<PortfolioDto>> updateHolding(@PathVariable String portfolioName,
                                                            @PathVariable String ticker,
                                                         @RequestBody @Valid Mono<UpdateHoldingRequest> request,
                                                         Principal principal) {
        return portfolioHoldingsService.updateHolding(portfolioName, ticker, request, principal)
                .map(portfolio ->  ResponseEntity.ok().body(portfolio));
    }

    @DeleteMapping("/{portfolioName}/holdings/{ticker}")
    public Mono<ResponseEntity<PortfolioDto>> deleteHolding(@PathVariable String portfolioName,
                                                            @PathVariable String ticker,
                                                            Principal principal) {
        return portfolioHoldingsService.removeHolding(portfolioName, ticker, principal)
                .map(portfolio ->  ResponseEntity.ok().body(portfolio));
    }
}
