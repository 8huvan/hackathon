package com.sih.advisor.service;

import com.sih.advisor.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Mock implementation of AIAdvisoryService.
 * Generates deterministic, realistic business advisory reports without using AI/ML models.
 *
 * This implementation provides structured, sensible mock data that demonstrates
 * the expected output format. A real AI service can replace this without changing
 * the controller or API contract.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MockAIAdvisoryService implements AIAdvisoryService {

    private final LocationMarketService locationMarketService;
    private final FinancialCalculationService financialCalculationService;

    @Override
    public FeasibilityReportDTO generateFeasibilityReport(AdvisoryRequestDTO request) {
        log.info("Generating feasibility report for {} in {}, {}",
                request.getBusinessCategory(), request.getVillage(), request.getDistrict());

        LocationDTO location = LocationDTO.builder()
                .village(request.getVillage())
                .block(request.getBlock())
                .district(request.getDistrict())
                .state(request.getState())
                .build();

        // Get market analysis from location service
        MarketAnalysisDTO marketAnalysis = locationMarketService.analyzeLocalMarket(
                location, request.getBusinessCategory());

        // Get financial breakdown from existing Phase 1 service
        FinancialBreakdownDTO financialSummary = financialCalculationService
                .calculateFinancialBreakdown(request.getAvailableMargin());

        // Generate SWOT analysis
        SwotAnalysisDTO swotAnalysis = generateSwotAnalysis(
                request.getBusinessCategory(), location, marketAnalysis);

        // Generate competitor analysis
        List<CompetitorDTO> competitors = generateCompetitorAnalysis(
                request.getBusinessCategory(), location);

        // Determine feasibility
        String feasibility = assessFeasibility(request);

        // Generate pricing and distribution guidance
        List<String> pricingGuidance = generatePricingGuidance(
                request.getBusinessCategory(), marketAnalysis);
        List<String> distributionChannels = marketAnalysis.getDistributionChannels();
        List<String> risks = generateRisks(request.getBusinessCategory(), marketAnalysis);

        // Generate recommendation summary
        String recommendationSummary = generateRecommendationSummary(
                request.getBusinessCategory(), location, feasibility, financialSummary);

        return FeasibilityReportDTO.builder()
                .location(location)
                .recommendedBusiness(request.getBusinessCategory())
                .recommendationSummary(recommendationSummary)
                .feasibilityAssessment(feasibility)
                .marketAnalysis(marketAnalysis)
                .swotAnalysis(swotAnalysis)
                .competitors(competitors)
                .pricingGuidance(pricingGuidance)
                .distributionChannels(distributionChannels)
                .risks(risks)
                .financialSummary(financialSummary)
                .build();
    }

    @Override
    public String assessFeasibility(AdvisoryRequestDTO request) {
        // Simple rule-based feasibility assessment
        BigDecimal projectCost = request.getAvailableMargin().divide(new BigDecimal("0.10"));

        // Check if capital is sufficient for the business
        if (projectCost.compareTo(new BigDecimal("50000")) < 0) {
            return "MODERATELY_FEASIBLE";
        } else if (projectCost.compareTo(new BigDecimal("500000")) < 0) {
            return "FEASIBLE";
        } else {
            return "HIGHLY_FEASIBLE";
        }
    }

    private SwotAnalysisDTO generateSwotAnalysis(String businessCategory,
                                                  LocationDTO location,
                                                  MarketAnalysisDTO marketAnalysis) {
        List<String> strengths = Arrays.asList(
                "Low competition in " + location.getVillage() + " area",
                "Strong local community relationships",
                "Lower operational costs compared to urban areas",
                "Growing rural market with increasing purchasing power",
                "Government support through rural entrepreneurship schemes"
        );

        List<String> weaknesses = Arrays.asList(
                "Limited initial capital may restrict inventory variety",
                "Lack of established brand recognition",
                "Potential supply chain challenges in rural areas",
                "Seasonal income variations affecting cash flow",
                "Limited access to skilled labor"
        );

        List<String> opportunities = Arrays.asList(
                "Expanding rural economy with rising disposable income",
                "Government schemes for rural business development",
                "Digital payment adoption enabling easier transactions",
                "Potential to serve neighboring villages (5-10 km radius)",
                "Low entry barriers in current market",
                "Growing demand for quality products in rural areas"
        );

        List<String> threats = Arrays.asList(
                "Potential entry of organized retail chains",
                "Competition from nearby town markets",
                "Economic downturns affecting rural purchasing power",
                "Seasonal agricultural cycles impacting demand",
                "Price sensitivity among target customers",
                "Infrastructure challenges affecting supply consistency"
        );

        return SwotAnalysisDTO.builder()
                .strengths(strengths)
                .weaknesses(weaknesses)
                .opportunities(opportunities)
                .threats(threats)
                .build();
    }

    private List<CompetitorDTO> generateCompetitorAnalysis(String businessCategory,
                                                            LocationDTO location) {
        // Generate 2-3 mock competitors based on business category
        String categoryLower = businessCategory.toLowerCase();

        if (categoryLower.contains("grocery") || categoryLower.contains("store")) {
            return Arrays.asList(
                    CompetitorDTO.builder()
                            .competitorName("Local Kirana Store")
                            .businessType("Traditional Grocery Store")
                            .pricePositioning("BUDGET")
                            .strengths("Established customer base, credit facility for regulars")
                            .weaknesses("Limited product variety, no modern amenities")
                            .competitiveAdvantage("Offer wider product range with quality assurance")
                            .distanceKm(2)
                            .build(),
                    CompetitorDTO.builder()
                            .competitorName("Weekly Village Market")
                            .businessType("Periodic Market")
                            .pricePositioning("BUDGET")
                            .strengths("Lower prices, variety from multiple vendors")
                            .weaknesses("Only available once a week, no convenience")
                            .competitiveAdvantage("Daily availability and consistent service")
                            .distanceKm(0)
                            .build(),
                    CompetitorDTO.builder()
                            .competitorName("Town Market Shops")
                            .businessType("Urban Retail")
                            .pricePositioning("MODERATE")
                            .strengths("Large inventory, established brands")
                            .weaknesses("10+ km distance, transportation cost for customers")
                            .competitiveAdvantage("Local convenience, personalized service")
                            .distanceKm(12)
                            .build()
            );
        } else {
            return Arrays.asList(
                    CompetitorDTO.builder()
                            .competitorName("Existing Local Business")
                            .businessType(businessCategory)
                            .pricePositioning("MODERATE")
                            .strengths("Established reputation, loyal customer base")
                            .weaknesses("Traditional approach, limited service hours")
                            .competitiveAdvantage("Modern approach with better customer service")
                            .distanceKm(3)
                            .build(),
                    CompetitorDTO.builder()
                            .competitorName("Nearby Town Competitors")
                            .businessType(businessCategory)
                            .pricePositioning("MODERATE")
                            .strengths("Larger scale, more resources")
                            .weaknesses("Distance, less personalized service")
                            .competitiveAdvantage("Local presence and community connection")
                            .distanceKm(10)
                            .build()
            );
        }
    }

    private List<String> generatePricingGuidance(String businessCategory,
                                                  MarketAnalysisDTO marketAnalysis) {
        return Arrays.asList(
                "Price products 5-10% below nearby town rates to attract price-sensitive customers",
                "Focus on value-for-money rather than premium pricing",
                "Offer combo deals and bulk purchase discounts for regular customers",
                "Consider flexible payment options (weekly/monthly credit) for trusted customers",
                "Maintain competitive pricing while ensuring sustainable profit margins (20-25%)",
                "Monitor seasonal demand and adjust pricing accordingly"
        );
    }

    private List<String> generateRisks(String businessCategory,
                                        MarketAnalysisDTO marketAnalysis) {
        return Arrays.asList(
                "Seasonal income variation: Agricultural cycles affect customer purchasing power",
                "Competition risk: Potential entry of larger organized retail chains",
                "Supply chain challenges: Ensuring consistent inventory in rural location",
                "Credit risk: Managing credit sales to customers during lean seasons",
                "Infrastructure: Road conditions may affect supply during monsoon",
                "Market saturation: Monitor local competition and adjust strategy accordingly"
        );
    }

    private String generateRecommendationSummary(String businessCategory,
                                                  LocationDTO location,
                                                  String feasibility,
                                                  FinancialBreakdownDTO financial) {
        return String.format(
                "Based on analysis of %s, %s district, starting a %s business is %s. " +
                "With available capital of ₹%s, you can establish a project worth ₹%s with a loan of ₹%s at %s%% interest. " +
                "The local market has %s competition and %s demand. Key success factors include maintaining competitive pricing, " +
                "building strong community relationships, and ensuring consistent product/service availability.",
                location.getVillage(),
                location.getDistrict(),
                businessCategory,
                feasibility.toLowerCase().replace("_", " "),
                financial.getAvailableMargin(),
                financial.getProjectCost(),
                financial.getActualLoanAmount(),
                financial.getApplicableScheme().getInterestRate(),
                "medium",
                "good"
        );
    }
}
