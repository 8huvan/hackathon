package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for complete business feasibility report.
 * Integrates all advisory components into a comprehensive response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeasibilityReportDTO {

    private LocationDTO location;
    private String recommendedBusiness;
    private String recommendationSummary;
    private String feasibilityAssessment; // HIGHLY_FEASIBLE, FEASIBLE, MODERATELY_FEASIBLE, NOT_FEASIBLE
    private MarketAnalysisDTO marketAnalysis;
    private SwotAnalysisDTO swotAnalysis;
    private List<CompetitorDTO> competitors;
    private List<String> pricingGuidance;
    private List<String> distributionChannels;
    private List<String> risks;
    private FinancialBreakdownDTO financialSummary;
}
