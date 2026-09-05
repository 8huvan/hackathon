package com.sih.advisor.service;

import com.sih.advisor.dto.AdvisoryRequestDTO;
import com.sih.advisor.dto.FeasibilityReportDTO;

/**
 * Service interface for AI-driven business advisory.
 * This abstraction allows different implementations (mock, real AI/ML models, external APIs).
 *
 * The service analyzes business feasibility based on location, business category,
 * and financial constraints, providing comprehensive advisory reports.
 */
public interface AIAdvisoryService {

    /**
     * Generates a comprehensive business feasibility report.
     *
     * @param request Advisory request containing location, business category, and capital
     * @return Complete feasibility report with market analysis, SWOT, competitors, and financial summary
     */
    FeasibilityReportDTO generateFeasibilityReport(AdvisoryRequestDTO request);

    /**
     * Determines if a business is feasible in the given context.
     *
     * @param request Advisory request
     * @return Feasibility assessment (HIGHLY_FEASIBLE, FEASIBLE, MODERATELY_FEASIBLE, NOT_FEASIBLE)
     */
    String assessFeasibility(AdvisoryRequestDTO request);
}
