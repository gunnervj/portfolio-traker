package com.tickertraker.portfoliotraker.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Holding> holdings;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastUpdatedDateTime;

}
