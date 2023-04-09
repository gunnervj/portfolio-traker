package com.tickertraker.portfoliotraker.mapper;

import com.tickertraker.portfoliotraker.controller.dto.*;
import com.tickertraker.portfoliotraker.repository.entity.Holding;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
import com.tickertraker.portfoliotraker.repository.projections.PortfolioMetaData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper( componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PortfolioMapper {
    Portfolio mapCreatePortfolioRequestToPortfolio(CreatePortfolioRequest createPortfolioRequest);

    @Mapping(target = "createdDate", source = "createdDateTime", dateFormat = "MM-dd-yyyy")
    CreatePortfolioResponse mapToCreatePortfolioResponse(Portfolio portfolio);

    @Mapping(target = "updatedDate", source = "lastUpdatedDateTime", dateFormat = "MM-dd-yyyy")
    RenamePortfolioResponse mapToRenamePortfolioResponse(Portfolio portfolio);

    @Mapping(target = "createdDate", source = "createdDateTime", dateFormat = "MM-dd-yyyy")
    @Mapping(target = "lastUpdatedDate", source = "lastUpdatedDateTime", dateFormat = "MM-dd-yyyy")
    PortfolioDto mapPortfolioToPortfolioDTO(Portfolio portfolio);

    @Mapping(target = "invPrice", source = "price")
    HoldingDto mapHoldingToHoldingDTO(Holding holding);

    @Mapping(target = "price", source = "invPrice")
    Holding mapUpdateHoldingRequestToHolding(UpdateHoldingRequest updateHoldingRequest);

    @Mapping(target = "createdDate", source = "createdDateTime", dateFormat = "MM-dd-yyyy")
    @Mapping(target = "lastUpdatedDate", source = "lastUpdatedDateTime", dateFormat = "MM-dd-yyyy")
    PortfolioMetaDataDto mapPortfolioToPortfolioMetaData(PortfolioMetaData portfolio);
}
