package com.sih.advisor.controller;

import com.sih.advisor.dto.*;
import com.sih.advisor.service.AIAdvisoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdvisoryController.
 */
@WebMvcTest(AdvisoryController.class)
class AdvisoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AIAdvisoryService aiAdvisoryService;

    private AdvisoryRequestDTO validRequest;
    private FeasibilityReportDTO mockReport;

    @BeforeEach
    void setUp() {
        validRequest = AdvisoryRequestDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .businessCategory("Grocery Store")
                .availableMargin(new BigDecimal("15000"))
                .build();

        LocationDTO location = LocationDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .build();

        MarketAnalysisDTO marketAnalysis = MarketAnalysisDTO.builder()
                .estimatedConsumerBase(6000)
                .marketReachKm(5)
                .distributionChannels(Arrays.asList("Direct retail", "Home delivery"))
                .underservedNiches(Arrays.asList("Quality-conscious families"))
                .localOpportunities(Arrays.asList("Growing economy"))
                .localThreats(Arrays.asList("Seasonal variations"))
                .pricingGuidance("Competitive pricing")
                .averageLocalIncome(new BigDecimal("15000"))
                .marketDemandLevel("HIGH")
                .competitionLevel("MEDIUM")
                .build();

        SwotAnalysisDTO swotAnalysis = SwotAnalysisDTO.builder()
                .strengths(Arrays.asList("Low competition", "Strong community"))
                .weaknesses(Arrays.asList("Limited capital"))
                .opportunities(Arrays.asList("Growing market"))
                .threats(Arrays.asList("Seasonal income"))
                .build();

        CompetitorDTO competitor = CompetitorDTO.builder()
                .competitorName("Local Store")
                .businessType("Grocery")
                .pricePositioning("BUDGET")
                .strengths("Established base")
                .weaknesses("Limited variety")
                .competitiveAdvantage("Better service")
                .distanceKm(2)
                .build();

        SchemeDetailsDTO scheme = SchemeDetailsDTO.builder()
                .schemeName("Micro Finance Scheme")
                .interestRate(new BigDecimal("6.5"))
                .tenureYears(3)
                .build();

        FinancialBreakdownDTO financialSummary = FinancialBreakdownDTO.builder()
                .availableMargin(new BigDecimal("15000"))
                .projectCost(new BigDecimal("150000"))
                .actualLoanAmount(new BigDecimal("125000"))
                .applicableScheme(scheme)
                .emiAmount(new BigDecimal("3831.13"))
                .build();

        mockReport = FeasibilityReportDTO.builder()
                .location(location)
                .recommendedBusiness("Grocery Store")
                .recommendationSummary("Feasible business opportunity")
                .feasibilityAssessment("FEASIBLE")
                .marketAnalysis(marketAnalysis)
                .swotAnalysis(swotAnalysis)
                .competitors(Arrays.asList(competitor))
                .pricingGuidance(Arrays.asList("Competitive pricing"))
                .distributionChannels(Arrays.asList("Direct retail"))
                .risks(Arrays.asList("Seasonal variations"))
                .financialSummary(financialSummary)
                .build();
    }

    @Test
    void testAnalyzeBusiness_Success() throws Exception {
        // Given
        when(aiAdvisoryService.generateFeasibilityReport(any(AdvisoryRequestDTO.class)))
                .thenReturn(mockReport);

        // When & Then
        mockMvc.perform(post("/api/advisory/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedBusiness").value("Grocery Store"))
                .andExpect(jsonPath("$.feasibilityAssessment").value("FEASIBLE"))
                .andExpect(jsonPath("$.location.village").value("Rampur"))
                .andExpect(jsonPath("$.marketAnalysis.estimatedConsumerBase").value(6000))
                .andExpect(jsonPath("$.swotAnalysis.strengths").isArray())
                .andExpect(jsonPath("$.competitors").isArray())
                .andExpect(jsonPath("$.financialSummary.availableMargin").value(15000));
    }

    @Test
    void testAnalyzeBusiness_MissingVillage() throws Exception {
        // Given
        AdvisoryRequestDTO invalidRequest = AdvisoryRequestDTO.builder()
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .businessCategory("Grocery Store")
                .availableMargin(new BigDecimal("15000"))
                .build();

        // When & Then
        mockMvc.perform(post("/api/advisory/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyzeBusiness_MissingBusinessCategory() throws Exception {
        // Given
        AdvisoryRequestDTO invalidRequest = AdvisoryRequestDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .availableMargin(new BigDecimal("15000"))
                .build();

        // When & Then
        mockMvc.perform(post("/api/advisory/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyzeBusiness_NegativeMargin() throws Exception {
        // Given
        AdvisoryRequestDTO invalidRequest = AdvisoryRequestDTO.builder()
                .village("Rampur")
                .block("Sadar")
                .district("Meerut")
                .state("Uttar Pradesh")
                .businessCategory("Grocery Store")
                .availableMargin(new BigDecimal("-1000"))
                .build();

        // When & Then
        mockMvc.perform(post("/api/advisory/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHealth() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/advisory/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Advisory service is running"));
    }
}
