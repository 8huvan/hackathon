package com.sih.advisor.controller;

import com.sih.advisor.dto.FinancialBreakdownDTO;
import com.sih.advisor.dto.FinancialInputDTO;
import com.sih.advisor.dto.RepaymentScheduleDTO;
import com.sih.advisor.service.FinancialCalculationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for financial calculations and scheme routing.
 */
@RestController
@RequestMapping("/api/financial")
@Validated
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class FinancialController {

    private static final Logger log = LoggerFactory.getLogger(FinancialController.class);

    private final FinancialCalculationService financialCalculationService;

    public FinancialController(FinancialCalculationService financialCalculationService) {
        this.financialCalculationService = financialCalculationService;
    }

    /**
     * Calculate financial breakdown based on available margin.
     *
     * POST /api/financial/calculate
     *
     * @param input FinancialInputDTO containing available margin
     * @return FinancialBreakdownDTO with complete financial details
     */
    @PostMapping("/calculate")
    public ResponseEntity<FinancialBreakdownDTO> calculateFinancialBreakdown(
            @Valid @RequestBody FinancialInputDTO input) {

        log.info("Received financial calculation request for margin: ₹{}", input.getAvailableMargin());

        FinancialBreakdownDTO breakdown = financialCalculationService.calculateFinancialBreakdown(
                input.getAvailableMargin()
        );

        log.info("Financial calculation completed successfully");
        return ResponseEntity.ok(breakdown);
    }

    /**
     * Generate detailed repayment schedule.
     *
     * GET /api/financial/repayment-schedule
     *
     * @param loanAmount Loan principal amount
     * @param interestRate Annual interest rate (e.g., 6.5 for 6.5%)
     * @param tenureYears Loan tenure in years
     * @param moratoriumMonths Moratorium period in months
     * @return RepaymentScheduleDTO with quarterly breakdown
     */
    @GetMapping("/repayment-schedule")
    public ResponseEntity<RepaymentScheduleDTO> getRepaymentSchedule(
            @RequestParam
            @DecimalMin(value = "1000.0", message = "Loan amount must be at least ₹1,000")
            BigDecimal loanAmount,

            @RequestParam
            @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
            @DecimalMin(value = "50.0", message = "Interest rate seems unreasonably high")
            BigDecimal interestRate,

            @RequestParam
            @Min(value = 1, message = "Tenure must be at least 1 year")
            @Max(value = 30, message = "Tenure cannot exceed 30 years")
            Integer tenureYears,

            @RequestParam
            @Min(value = 0, message = "Moratorium cannot be negative")
            @Max(value = 24, message = "Moratorium cannot exceed 24 months")
            Integer moratoriumMonths) {

        log.info("Generating repayment schedule - Loan: ₹{}, Rate: {}%, Tenure: {} years, Moratorium: {} months",
                loanAmount, interestRate, tenureYears, moratoriumMonths);

        RepaymentScheduleDTO schedule = financialCalculationService.generateRepaymentSchedule(
                loanAmount,
                interestRate,
                tenureYears,
                moratoriumMonths
        );

        log.info("Repayment schedule generated successfully with {} installments", schedule.getSchedule().size());
        return ResponseEntity.ok(schedule);
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Financial service is running");
    }
}
