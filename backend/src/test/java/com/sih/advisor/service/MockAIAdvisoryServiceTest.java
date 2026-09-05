package com.sih.advisor.service;

import com.sih.advisor.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MockAIAdvisoryService.
 */
@ExtendWith(MockitoExtension.class)
class MockAIAdvisoryServiceTest {

    @Mock
    private LocationMarketService locationMarketService;

    @Mock
    private FinancialCalculationService financialCalculationService;

    @InjectMocks
    private MockAIAdvisoryService aiAdvisoryService;

    private AdvisoryRequestDTO request;
    private MarketAnalysisDTO mockMarketAnalysis;
    private FinancialBreakdownDTO mockFinancialBreakdown;

    @BeforeEach
    void setUp() {
        request = AdvisoryRequestDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .businessCategory("Grocery Store")
                .availableMargin(new BigDecimal("15000"))
                .build();

        mockMarketAnalysis = MarketAnalysisDTO.builder()
                .estimatedConsumerBase(6000)
                .marketReachKm(5)
                .marketDemandLevel("HIGH")
                .competitionLevel("MEDIUM")
                .build();

        SchemeDetailsDTO scheme = SchemeDetailsDTO.builder()
                .schemeName("Micro Finance Scheme")
                .interestRate(new BigDecimal("6.5"))
                .build();

        mockFinancialBreakdown = FinancialBreakdownDTO.builder()
                .availableMargin(new BigDecimal("15000"))
                .projectCost(new BigDecimal("150000"))
                .actualLoanAmount(new BigDecimal("125000"))
                .applicableScheme(scheme)
                .build();
    }

    @Test
    void testGenerateFeasibilityReport_Success() {
        // Given
        when(locationMarketService.analyzeLocalMarket(any(LocationDTO.class), anyString()))
                .thenReturn(mockMarketAnalysis);
        when(financialCalculationService.calculateFinancialBreakdown(any(BigDecimal.class)))
                .thenReturn(mockFinancialBreakdown);

        // When
        FeasibilityReportDTO report = aiAdvisoryService.generateFeasibilityReport(request);

        // Then
        assertNotNull(report);
        assertEquals("Grocery Store", report.getRecommendedBusiness());
        assertNotNull(report.getRecommendationSummary());
        assertNotNull(report.getFeasibilityAssessment());
        assertNotNull(report.getMarketAnalysis());
        assertNotNull(report.getSwotAnalysis());
        assertNotNull(report.getCompetitors());
        assertNotNull(report.getPricingGuidance());
        assertNotNull(report.getDistributionChannels());
        assertNotNull(report.getRisks());
        assertNotNull(report.getFinancialSummary());

        verify(locationMarketService).analyzeLocalMarket(any(LocationDTO.class), eq("Grocery Store"));
        verify(financialCalculationService).calculateFinancialBreakdown(new BigDecimal("15000"));
    }

    @Test
    void testAssessFeasibility_ModeratelyFeasible() {
        // Small capital - project cost < 50,000
        AdvisoryRequestDTO smallCapitalRequest = AdvisoryRequestDTO.builder()
                .availableMargin(new BigDecimal("3000")) // Project cost = 30,000
                .businessCategory("Small Shop")
                .village("Test")
                .block("Test")
                .district("Test")
                .state("Test")
                .build();

        String feasibility = aiAdvisoryService.assessFeasibility(smallCapitalRequest);
        assertEquals("MODERATELY_FEASIBLE", feasibility);
    }

    @Test
    void testAssessFeasibility_Feasible() {
        // Medium capital - project cost between 50,000 and 500,000
        AdvisoryRequestDTO mediumCapitalRequest = AdvisoryRequestDTO.builder()
                .availableMargin(new BigDecimal("20000")) // Project cost = 200,000
                .businessCategory("Grocery Store")
                .village("Test")
                .block("Test")
                .district("Test")
                .state("Test")
                .build();

        String feasibility = aiAdvisoryService.assessFeasibility(mediumCapitalRequest);
        assertEquals("FEASIBLE", feasibility);
    }

    @Test
    void testAssessFeasibility_HighlyFeasible() {
        // Large capital - project cost > 500,000
        AdvisoryRequestDTO largeCapitalRequest = AdvisoryRequestDTO.builder()
                .availableMargin(new BigDecimal("75000")) // Project cost = 750,000
                .businessCategory("Large Store")
                .village("Test")
                .block("Test")
                .district("Test")
                .state("Test")
                .build();

        String feasibility = aiAdvisoryService.assessFeasibility(largeCapitalRequest);
        assertEquals("HIGHLY_FEASIBLE", feasibility);
    }

    @Test
    void testGenerateFeasibilityReport_SwotAnalysisStructure() {
        // Given
        when(locationMarketService.analyzeLocalMarket(any(LocationDTO.class), anyString()))
                .thenReturn(mockMarketAnalysis);
        when(financialCalculationService.calculateFinancialBreakdown(any(BigDecimal.class)))
                .thenReturn(mockFinancialBreakdown);

        // When
        FeasibilityReportDTO report = aiAdvisoryService.generateFeasibilityReport(request);

        // Then
        SwotAnalysisDTO swot = report.getSwotAnalysis();
        assertNotNull(swot);
        assertNotNull(swot.getStrengths());
        assertNotNull(swot.getWeaknesses());
        assertNotNull(swot.getOpportunities());
        assertNotNull(swot.getThreats());
        assertFalse(swot.getStrengths().isEmpty());
        assertFalse(swot.getWeaknesses().isEmpty());
        assertFalse(swot.getOpportunities().isEmpty());
        assertFalse(swot.getThreats().isEmpty());
    }

    @Test
    void testGenerateFeasibilityReport_CompetitorAnalysis() {
        // Given
        when(locationMarketService.analyzeLocalMarket(any(LocationDTO.class), anyString()))
                .thenReturn(mockMarketAnalysis);
        when(financialCalculationService.calculateFinancialBreakdown(any(BigDecimal.class)))
                .thenReturn(mockFinancialBreakdown);

        // When
        FeasibilityReportDTO report = aiAdvisoryService.generateFeasibilityReport(request);

        // Then
        assertNotNull(report.getCompetitors());
        assertFalse(report.getCompetitors().isEmpty());
        assertTrue(report.getCompetitors().size() >= 2);

        CompetitorDTO competitor = report.getCompetitors().get(0);
        assertNotNull(competitor.getCompetitorName());
        assertNotNull(competitor.getBusinessType());
        assertNotNull(competitor.getPricePositioning());
        assertNotNull(competitor.getStrengths());
        assertNotNull(competitor.getWeaknesses());
        assertNotNull(competitor.getCompetitiveAdvantage());
    }

    @Test
    void testGenerateFeasibilityReport_PricingAndRisks() {
        // Given
        when(locationMarketService.analyzeLocalMarket(any(LocationDTO.class), anyString()))
                .thenReturn(mockMarketAnalysis);
        when(financialCalculationService.calculateFinancialBreakdown(any(BigDecimal.class)))
                .thenReturn(mockFinancialBreakdown);

        // When
        FeasibilityReportDTO report = aiAdvisoryService.generateFeasibilityReport(request);

        // Then
        assertNotNull(report.getPricingGuidance());
        assertFalse(report.getPricingGuidance().isEmpty());
        assertNotNull(report.getRisks());
        assertFalse(report.getRisks().isEmpty());
    }
}
