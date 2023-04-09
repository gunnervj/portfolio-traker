package com.tickertraker.portfoliotraker.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.summingDouble;

@Document
@Getter
@Setter
@ToString
@CompoundIndexes({
        @CompoundIndex(name = "portfolioNameAndOwner", def = "{'owner' : 1, 'portfolioName': 1}")
})
public class Portfolio {
    @Id
    private String id;
    private String owner;
    private String portfolioName;
    private Set<Holding> holdings;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastUpdatedDateTime;
    private Double unrealizedGainLoss;

    public void reCalculateUnrealizedGainLoss() {
        if (this.getHoldings() != null) {
            double unrealizedGainLoss = this.getHoldings()
                    .stream()
                    .map(holdingDto -> holdingDto.getUnrealizedGainLoss())
                    .collect(summingDouble(f -> f));
            this.setUnrealizedGainLoss(unrealizedGainLoss);
        }
    }
}
