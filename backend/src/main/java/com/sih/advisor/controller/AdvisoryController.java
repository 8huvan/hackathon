package com.sih.advisor.controller;

import com.sih.advisor.dto.AdvisoryRequestDTO;
import com.sih.advisor.dto.FeasibilityReportDTO;
import com.sih.advisor.service.AIAdvisoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for business advisory endpoints.
 * Provides hyper-local business feasibility analysis and recommendations.
 */
@RestController
@RequestMapping("/api/advisory")
@RequiredArgsConstructor
@Slf4j
public class AdvisoryController {

    private final AIAdvisoryService aiAdvisoryService;

    /**
     * Analyzes business feasibility for a given location and business category.
     *
     * @param request Advisory request containing location, business category, and available capital
     * @return Complete feasibility report with market analysis, SWOT, competitors, and financial summary
     */
    @PostMapping("/analyze")
    public ResponseEntity<FeasibilityReportDTO> analyzeBusiness(@Valid @RequestBody AdvisoryRequestDTO request) {
        log.info("Received advisory request for {} in {}, {} with capital: ₹{}",
                request.getBusinessCategory(),
                request.getVillage(),
                request.getDistrict(),
                request.getAvailableMargin());

        FeasibilityReportDTO report = aiAdvisoryService.generateFeasibilityReport(request);

        log.info("Advisory report generated successfully. Feasibility: {}",
                report.getFeasibilityAssessment());

        return ResponseEntity.ok(report);
    }

    /**
     * Health check endpoint for advisory service.
     *
     * @return Health status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Advisory service is running");
    }
}
