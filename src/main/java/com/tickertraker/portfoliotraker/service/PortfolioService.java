package com.tickertraker.portfoliotraker.service;

import com.tickertraker.portfoliotraker.controller.dto.*;
import com.tickertraker.portfoliotraker.core.constants.ErrorMessages;
import com.tickertraker.portfoliotraker.exception.PortfolioTrakerException;
import com.tickertraker.portfoliotraker.exception.UnknownException;
import com.tickertraker.portfoliotraker.helper.PortfolioGainLossHelper;
import com.tickertraker.portfoliotraker.mapper.PortfolioMapper;
import com.tickertraker.portfoliotraker.repository.PortfolioRepository;
import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    private final PortfolioGainLossHelper portfolioGainLossHelper;

    public Mono<CreatePortfolioResponse> createPortfolio(Mono<CreatePortfolioRequest> request, Principal principal) {
        return request.flatMap(createPortfolioRequest -> this.createPortfolio(createPortfolioRequest, principal));
    }

    public Mono<RenamePortfolioResponse> renamePortfolio(Mono<RenamePortfolioRequest> request,
                                                         String existingPortfolioName,
                                                         Principal principal) {
        return request.flatMap(renamePortfolioRequest -> this.renamePortfolio(renamePortfolioRequest,
                existingPortfolioName, principal));
    }


    public Flux<PortfolioMetaDataDto> getAllPortfolios(Principal principal) {
        return portfolioRepository.findPortfolioByOwner(principal.getName())
                .map(portfolioMapper::mapPortfolioToPortfolioMetaData);
    }

    public Mono<PortfolioDto> getPortfolio(String portfolioName,
                                           Principal principal) {
        return portfolioRepository.findPortfolioByPortfolioNameAndOwner(portfolioName, principal.getName())
                .flatMap(portfolioGainLossHelper::calculateGainLoss)
                .map(portfolioMapper::mapPortfolioToPortfolioDTO);
    }

    public Mono<Void> deletePortfolio(String portfolioName, Principal principal) {
        return portfolioRepository.existsPortfolioByPortfolioNameAndOwner(portfolioName, principal.getName())
                .filter(exists -> exists)
                .flatMap(exists -> portfolioRepository.deletePortfolioByPortfolioNameAndOwner(portfolioName, principal.getName()))
                .onErrorResume(ex -> {
                    log.error("Error while saving portfolio to db : {}", ex.getMessage(), ex);
                    throw new UnknownException(ErrorMessages.ERROR_PORTFOLIO_DELETION);
                });
    }

    private Mono<CreatePortfolioResponse> createPortfolio(CreatePortfolioRequest request, Principal principal) {
       return portfolioRepository.existsPortfolioByPortfolioNameAndOwner(request.getPortfolioName(), principal.getName())
               .filter(exists -> !exists)
               .map(exists -> portfolioMapper.mapCreatePortfolioRequestToPortfolio(request))
               .map(portfolio -> this.nourishPortfolio(portfolio, principal.getName()))
               .flatMap(portfolioRepository::save)
               .map(portfolioMapper::mapToCreatePortfolioResponse)
               .onErrorResume(ex -> {
                       log.error("Error while saving portfolio to db : {}", ex.getMessage(), ex);
                       throw new UnknownException(ErrorMessages.ERROR_PORTFOLIO_CREATION);
               })
               .switchIfEmpty(Mono.error(createDuplicateException()));
    }

    private Mono<RenamePortfolioResponse> renamePortfolio(RenamePortfolioRequest request,
                                                          String existingPortfolioName,
                                                          Principal principal) {
        return portfolioRepository.findPortfolioByPortfolioNameAndOwner(existingPortfolioName, principal.getName())
                .map(portfolio -> {
                    portfolio.setPortfolioName(request.getPortfolioName());
                    portfolio.setLastUpdatedDateTime(LocalDateTime.now());
                    return portfolio;
                })
                .flatMap(portfolioRepository::save)
                .map(portfolioMapper::mapToRenamePortfolioResponse)
                .onErrorResume(ex -> {
                    log.error("Error while renaming portfolio : {}", ex.getMessage(), ex);
                    throw new UnknownException(ErrorMessages.ERROR_PORTFOLIO_RENAME);
                })
                .switchIfEmpty(Mono.error(new PortfolioTrakerException(ErrorMessages.ERROR_PORTFOLIO_RENAME,
                        HttpStatus.BAD_REQUEST)));
    }

    private PortfolioTrakerException createDuplicateException() {
        return new PortfolioTrakerException(ErrorMessages.ERROR_PORTFOLIO_CREATION_DUPLICATE, HttpStatus.BAD_REQUEST);
    }

    private Portfolio nourishPortfolio(Portfolio portfolio, String userName) {
        if (null != portfolio) {
            Holding holding = new Holding();
            holding.setTicker("AAPL");
            holding.setQuantity(1);
            holding.setDescription("APPLE");
            holding.setUnrealizedGainLoss(111D);
            holding.setPrice(12D);
            portfolio.setHoldings(new HashSet<>());
            portfolio.getHoldings().add(holding);
            portfolio.setCreatedDateTime(LocalDateTime.now());
            portfolio.setOwner(userName);
            portfolio.setLastUpdatedDateTime(LocalDateTime.now());
        }
        log.info("Portfolio Information Nourished");
        return portfolio;
    }
}
