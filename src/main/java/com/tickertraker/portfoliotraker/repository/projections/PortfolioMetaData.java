package com.tickertraker.portfoliotraker.repository.projections;

import java.time.LocalDateTime;

public interface PortfolioMetaData {
    String getOwner();
    String getPortfolioName();
    LocalDateTime getCreatedDateTime();
    LocalDateTime getLastUpdatedDateTime();
}
