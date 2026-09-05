package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for competitor information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitorDTO {

    private String competitorName;
    private String businessType;
    private String pricePositioning; // PREMIUM, MODERATE, BUDGET
    private String strengths;
    private String weaknesses;
    private String competitiveAdvantage;
    private Integer distanceKm;
}
