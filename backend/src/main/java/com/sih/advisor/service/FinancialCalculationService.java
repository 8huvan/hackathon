package com.sih.advisor.service;

import com.sih.advisor.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Main service for financial calculations and breakdown generation.
 * Orchestrates scheme routing, EMI calculation, and repayment schedule generation.
 */
@Service
public class FinancialCalculationService {

    private static final Logger log = LoggerFactory.getLogger(FinancialCalculationService.class);

    private final SchemeRouterService schemeRouterService;
    private final EMICalculatorService emiCalculatorService;

    public FinancialCalculationService(SchemeRouterService schemeRouterService, EMICalculatorService emiCalculatorService) {
        this.schemeRouterService = schemeRouterService;
        this.emiCalculatorService = emiCalculatorService;
    }

    private static final BigDecimal MARGIN_PERCENTAGE = new BigDecimal("0.10");
    private static final BigDecimal LOAN_PERCENTAGE = new BigDecimal("0.90");
    private static final BigDecimal WORKING_CAPITAL_PERCENTAGE = new BigDecimal("0.15");
    private static final BigDecimal OPERATIONAL_COST_PERCENTAGE = new BigDecimal("0.20");
    private static final int SCALE = 2;

    /**
     * Calculates complete financial breakdown based on available margin.
     *
     * Logic:
     * - Project Cost = Available Margin / 0.10
     * - Maximum Loan = Project Cost × 0.90
     * - Route to appropriate scheme based on project cost
     * - Actual loan is capped at scheme's max funding
     * - Calculate EMI and total repayment
     *
     * @param availableMargin User's available capital
     * @return Complete financial breakdown with scheme details
     */
    public FinancialBreakdownDTO calculateFinancialBreakdown(BigDecimal availableMargin) {
        log.info("Calculating financial breakdown for margin: ₹{}", availableMargin);

        // Step 1: Calculate project cost
        BigDecimal projectCost = availableMargin.divide(MARGIN_PERCENTAGE, SCALE, RoundingMode.HALF_UP);
        log.debug("Project cost calculated: ₹{}", projectCost);

        // Step 2: Calculate maximum loan (90% of project cost)
        BigDecimal maxLoanAmount = projectCost.multiply(LOAN_PERCENTAGE)
                .setScale(SCALE, RoundingMode.HALF_UP);
        log.debug("Maximum loan amount: ₹{}", maxLoanAmount);

        // Step 3: Determine applicable scheme
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);
        log.info("Scheme determined: {}", scheme.getSchemeName());

        // Step 4: Cap actual loan at scheme's max funding
        BigDecimal actualLoanAmount = maxLoanAmount.min(scheme.getMaxFunding());
        if (actualLoanAmount.compareTo(maxLoanAmount) < 0) {
            log.warn("Loan amount capped at scheme maximum: ₹{}", actualLoanAmount);
        }

        // Step 5: Calculate EMI
        BigDecimal emi = emiCalculatorService.calculateEMI(
                actualLoanAmount,
                scheme.getInterestRate(),
                scheme.getTenureYears()
        );
        log.debug("EMI calculated: ₹{}", emi);

        // Step 6: Calculate total repayment
        BigDecimal totalRepayment = emiCalculatorService.calculateTotalRepayment(
                emi,
                scheme.getTenureYears()
        );
        BigDecimal totalInterest = totalRepayment.subtract(actualLoanAmount)
                .setScale(SCALE, RoundingMode.HALF_UP);

        int totalInstallments = scheme.getTenureYears() * 12;

        // Step 7: Calculate estimated working capital and operational costs
        BigDecimal estimatedWorkingCapital = projectCost.multiply(WORKING_CAPITAL_PERCENTAGE)
                .setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal estimatedOperationalCost = projectCost.multiply(OPERATIONAL_COST_PERCENTAGE)
                .setScale(SCALE, RoundingMode.HALF_UP);

        String message = buildMessage(actualLoanAmount, maxLoanAmount, scheme);

        return FinancialBreakdownDTO.builder()
                .availableMargin(availableMargin)
                .projectCost(projectCost)
                .maxLoanAmount(maxLoanAmount)
                .actualLoanAmount(actualLoanAmount)
                .applicableScheme(scheme)
                .emiAmount(emi)
                .totalRepayment(totalRepayment)
                .totalInterest(totalInterest)
                .totalInstallments(totalInstallments)
                .estimatedWorkingCapital(estimatedWorkingCapital)
                .estimatedOperationalCost(estimatedOperationalCost)
                .message(message)
                .build();
    }

    /**
     * Generates detailed repayment schedule with quarterly breakdown.
     *
     * @param loanAmount Loan principal
     * @param interestRate Annual interest rate
     * @param tenureYears Loan tenure
     * @param moratoriumMonths Moratorium period
     * @return Repayment schedule with quarterly installments
     */
    public RepaymentScheduleDTO generateRepaymentSchedule(
            BigDecimal loanAmount,
            BigDecimal interestRate,
            int tenureYears,
            int moratoriumMonths) {

        log.info("Generating repayment schedule for loan: ₹{}", loanAmount);

        BigDecimal emi = emiCalculatorService.calculateEMI(loanAmount, interestRate, tenureYears);
        BigDecimal totalRepayment = emiCalculatorService.calculateTotalRepayment(emi, tenureYears);
        BigDecimal totalInterest = totalRepayment.subtract(loanAmount)
                .setScale(SCALE, RoundingMode.HALF_UP);
        int totalInstallments = tenureYears * 12;

        List<RepaymentInstallmentDTO> schedule = emiCalculatorService.generateRepaymentSchedule(
                loanAmount,
                interestRate,
                tenureYears,
                moratoriumMonths
        );

        return RepaymentScheduleDTO.builder()
                .loanAmount(loanAmount)
                .interestRate(interestRate)
                .tenureYears(tenureYears)
                .moratoriumMonths(moratoriumMonths)
                .emiAmount(emi)
                .totalRepayment(totalRepayment)
                .totalInterest(totalInterest)
                .totalInstallments(totalInstallments)
                .schedule(schedule)
                .build();
    }

    private String buildMessage(BigDecimal actualLoan, BigDecimal maxLoan, SchemeDetailsDTO scheme) {
        if (actualLoan.compareTo(maxLoan) < 0) {
            return String.format(
                "Your loan amount is capped at ₹%s due to %s maximum funding limit.",
                actualLoan, scheme.getSchemeName()
            );
        }
        return String.format("You are eligible for %s with complete loan coverage.", scheme.getSchemeName());
    }
}
