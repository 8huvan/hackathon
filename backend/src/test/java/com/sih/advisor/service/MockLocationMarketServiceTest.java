package com.sih.advisor.service;

import com.sih.advisor.dto.LocationDTO;
import com.sih.advisor.dto.MarketAnalysisDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MockLocationMarketService.
 */
class MockLocationMarketServiceTest {

    private MockLocationMarketService locationMarketService;
    private LocationDTO location;

    @BeforeEach
    void setUp() {
        locationMarketService = new MockLocationMarketService();
        location = LocationDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .build();
    }

    @Test
    void testAnalyzeLocalMarket_GroceryStore() {
        // When
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Grocery Store");

        // Then
        assertNotNull(analysis);
        assertNotNull(analysis.getEstimatedConsumerBase());
        assertTrue(analysis.getEstimatedConsumerBase() > 0);
        assertNotNull(analysis.getMarketReachKm());
        assertNotNull(analysis.getDistributionChannels());
        assertFalse(analysis.getDistributionChannels().isEmpty());
        assertNotNull(analysis.getMarketDemandLevel());
        assertNotNull(analysis.getCompetitionLevel());
    }

    @Test
    void testEstimateConsumerBase() {
        // When
        Integer consumerBase = locationMarketService.estimateConsumerBase(location);

        // Then
        assertNotNull(consumerBase);
        assertTrue(consumerBase >= 2000);
        assertTrue(consumerBase <= 8000);
    }

    @Test
    void testEstimateConsumerBase_Deterministic() {
        // Same location should give same result
        Integer base1 = locationMarketService.estimateConsumerBase(location);
        Integer base2 = locationMarketService.estimateConsumerBase(location);

        assertEquals(base1, base2);
    }

    @Test
    void testDetermineMarketRadius_GroceryStore() {
        // Daily needs items have smaller radius
        Integer radius = locationMarketService.determineMarketRadius("Grocery Store");

        assertNotNull(radius);
        assertEquals(3, radius);
    }

    @Test
    void testDetermineMarketRadius_Hardware() {
        // Occasional purchase items have larger radius
        Integer radius = locationMarketService.determineMarketRadius("Hardware Store");

        assertNotNull(radius);
        assertEquals(8, radius);
    }

    @Test
    void testDetermineMarketRadius_Default() {
        // Unknown category gets default radius
        Integer radius = locationMarketService.determineMarketRadius("Unknown Business");

        assertNotNull(radius);
        assertEquals(7, radius);
    }

    @Test
    void testAnalyzeLocalMarket_MarketDemandLevels() {
        // High demand category
        MarketAnalysisDTO groceryAnalysis = locationMarketService.analyzeLocalMarket(
                location, "Grocery Store");
        assertEquals("HIGH", groceryAnalysis.getMarketDemandLevel());

        // Medium demand category
        MarketAnalysisDTO hardwareAnalysis = locationMarketService.analyzeLocalMarket(
                location, "Hardware Store");
        assertEquals("MEDIUM", hardwareAnalysis.getMarketDemandLevel());
    }

    @Test
    void testAnalyzeLocalMarket_CompetitionLevels() {
        // Grocery has medium competition
        MarketAnalysisDTO groceryAnalysis = locationMarketService.analyzeLocalMarket(
                location, "Grocery Store");
        assertEquals("MEDIUM", groceryAnalysis.getCompetitionLevel());

        // Electronics has low competition
        MarketAnalysisDTO electronicsAnalysis = locationMarketService.analyzeLocalMarket(
                location, "Electronics Store");
        assertEquals("LOW", electronicsAnalysis.getCompetitionLevel());
    }

    @Test
    void testAnalyzeLocalMarket_UnderservedNiches() {
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Retail Store");

        assertNotNull(analysis.getUnderservedNiches());
        assertFalse(analysis.getUnderservedNiches().isEmpty());
    }

    @Test
    void testAnalyzeLocalMarket_LocalOpportunities() {
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Retail Store");

        assertNotNull(analysis.getLocalOpportunities());
        assertFalse(analysis.getLocalOpportunities().isEmpty());
        assertTrue(analysis.getLocalOpportunities().stream()
                .anyMatch(opp -> opp.contains(location.getVillage())));
    }

    @Test
    void testAnalyzeLocalMarket_LocalThreats() {
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Retail Store");

        assertNotNull(analysis.getLocalThreats());
        assertFalse(analysis.getLocalThreats().isEmpty());
    }

    @Test
    void testAnalyzeLocalMarket_PricingGuidance() {
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Retail Store");

        assertNotNull(analysis.getPricingGuidance());
        assertFalse(analysis.getPricingGuidance().isEmpty());
    }

    @Test
    void testAnalyzeLocalMarket_AverageIncome() {
        MarketAnalysisDTO analysis = locationMarketService.analyzeLocalMarket(location, "Retail Store");

        assertNotNull(analysis.getAverageLocalIncome());
        assertTrue(analysis.getAverageLocalIncome().compareTo(java.math.BigDecimal.ZERO) > 0);
    }
}
