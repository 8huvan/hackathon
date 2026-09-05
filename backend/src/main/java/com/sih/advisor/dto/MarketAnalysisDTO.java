package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for market analysis data.
 * Contains structured market information for the business location.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketAnalysisDTO {

    private Integer estimatedConsumerBase;
    private Integer marketReachKm;
    private List<String> distributionChannels;
    private List<String> underservedNiches;
    private List<String> localOpportunities;
    private List<String> localThreats;
    private String pricingGuidance;
    private BigDecimal averageLocalIncome;
    private String marketDemandLevel; // HIGH, MEDIUM, LOW
    private String competitionLevel; // HIGH, MEDIUM, LOW
}
