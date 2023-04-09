package com.tickertraker.portfoliotraker.service;

import com.tickertraker.portfoliotraker.controller.dto.AddHoldingRequest;
import com.tickertraker.portfoliotraker.controller.dto.PortfolioDto;
import com.tickertraker.portfoliotraker.controller.dto.UpdateHoldingRequest;
import com.tickertraker.portfoliotraker.core.constants.Tickers;
import com.tickertraker.portfoliotraker.exception.PortfolioTrakerException;
import com.tickertraker.portfoliotraker.mapper.PortfolioMapper;
import com.tickertraker.portfoliotraker.repository.CurrentTickerPriceRepository;
import com.tickertraker.portfoliotraker.repository.PortfolioRepository;
import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioHoldingsService {
    private final PortfolioMapper portfolioMapper;
    private final CurrentTickerPriceRepository currentTickerPriceRepository;
    private final PortfolioRepository portfolioRepository;

    public Mono<PortfolioDto> addHolding(String portfolioName,
                                         Mono<AddHoldingRequest> addRequest,
                                         Principal principal) {
        return addRequest.map(request -> {
                    Holding holding = new Holding();
                    holding.setTicker(request.getTicker());
                    holding.setDescription(Tickers.getTicker(request.getTicker()).getDescription());
                    holding.setPrice(request.getInvPrice());
                    holding.setQuantity(request.getQuantity());
                    return holding;
                })
                .flatMap(this::setUnrealizedGainLossForHolding)
                .flatMap(holding -> portfolioRepository.addNewHoldingToPortfolio(portfolioName, principal.getName(), holding))
                .onErrorResume( error -> {
                    log.error("Error while Adding Holding:: {}", error.getMessage(), error);
                    throw new PortfolioTrakerException("Cannot add Holding to Portfolio.", HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .switchIfEmpty(Mono.error(new PortfolioTrakerException("Cannot add Holding to Portfolio.", HttpStatus.BAD_REQUEST)))
                .map(portfolio -> {
                    portfolio.reCalculateUnrealizedGainLoss();
                    portfolioRepository.savePortfolioUnrealizedGainLoss(portfolioName, principal.getName(), portfolio.getUnrealizedGainLoss());
                    return portfolio;
                })
                .map(portfolioMapper::mapPortfolioToPortfolioDTO);
    }

    public Mono<PortfolioDto> updateHolding(String portfolioName,
                                            String ticker,
                                            Mono<UpdateHoldingRequest> updateHoldingRequest,
                                            Principal principal) {
        return portfolioRepository.findPortfolioByPortfolioNameAndOwner(portfolioName, principal.getName())
                .map(portfolio -> portfolio.getHoldings()
                        .stream()
                        .filter(holding -> holding.getTicker().equalsIgnoreCase(ticker))
                        .findFirst()
                        .orElseThrow(() -> new PortfolioTrakerException("Error Updating Portfolio", HttpStatus.BAD_REQUEST)))
                .flatMap(holding -> updateHolding(holding, updateHoldingRequest,portfolioName, principal))
                .map(portfolio -> {
                    portfolio.reCalculateUnrealizedGainLoss();
                    portfolioRepository.savePortfolioUnrealizedGainLoss(portfolioName, principal.getName(), portfolio.getUnrealizedGainLoss());
                    return portfolio;
                })
                .map(portfolioMapper::mapPortfolioToPortfolioDTO);
    }

    private Mono<Portfolio> updateHolding(Holding holding,
                                          Mono<UpdateHoldingRequest> updateHoldingRequest,
                                          String portfolioName,
                                          Principal principal) {
        return updateHoldingRequest.map(request -> {
                if (request.getQuantity() != null && request.getQuantity() > 0 )
                    holding.setQuantity(request.getQuantity());
                if (request.getInvPrice() != null && request.getInvPrice() > 0 )
                    holding.setPrice(request.getInvPrice());
                return holding;
            })
            .flatMap(this::setUnrealizedGainLossForHolding)
            .flatMap(hold -> portfolioRepository.updateHoldingInPortfolio(portfolioName, principal.getName(), hold));
    }

    public Mono<PortfolioDto> removeHolding(String portfolioName,
                                            String ticker,
                                            Principal principal) {
        return portfolioRepository.removeHoldingInPortfolio(portfolioName, principal.getName(), ticker)
                .onErrorResume( error -> {
                    log.error("Error while Removing Holding:: {}", error.getMessage(), error);
                    throw new PortfolioTrakerException("Cannot remove Holding from Portfolio.", HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .switchIfEmpty(Mono.error(new PortfolioTrakerException("Cannot remove Holding from Portfolio.", HttpStatus.BAD_REQUEST)))
                .map(portfolioMapper::mapPortfolioToPortfolioDTO);
    }

    private Mono<Holding> setUnrealizedGainLossForHolding(Holding holding) {
        log.info("Calculating unrealized Gain loss for holding: {}", holding);
        return currentTickerPriceRepository.findById(holding.getTicker())
                .map(currentTickerPrice -> {
                    log.info("Current Price : {}", currentTickerPrice);
                    holding.recalculateGainLoss(currentTickerPrice.getCurrentPrice());
                    return holding;
                });
    }
}
