package com.tickertraker.portfoliotraker.mapper;

import com.tickertraker.portfoliotraker.controller.dto.CreatePortfolioRequest;
import com.tickertraker.portfoliotraker.controller.dto.CreatePortfolioResponse;
import com.tickertraker.portfoliotraker.controller.dto.RenamePortfolioRequest;
import com.tickertraker.portfoliotraker.controller.dto.RenamePortfolioResponse;
import com.tickertraker.portfoliotraker.repository.entity.Portfolio;
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
}
