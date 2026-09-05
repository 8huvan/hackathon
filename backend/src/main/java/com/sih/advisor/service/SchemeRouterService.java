package com.sih.advisor.service;

import com.sih.advisor.dto.SchemeDetailsDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for routing loan applications to appropriate government schemes
 * based on project cost calculations.
 *
 * Rules:
 * - Project Cost <= ₹1.40 lakh → Micro Finance Scheme
 * - Project Cost > ₹1.40 lakh AND <= ₹50 lakh → Term Loan Scheme
 */
@Service
public class SchemeRouterService {

    // Scheme thresholds
    private static final BigDecimal MICRO_FINANCE_THRESHOLD = new BigDecimal("140000");
    private static final BigDecimal TERM_LOAN_THRESHOLD = new BigDecimal("5000000");

    // Micro Finance Scheme constants
    private static final BigDecimal MICRO_FINANCE_MAX_FUNDING = new BigDecimal("125000");
    private static final BigDecimal MICRO_FINANCE_INTEREST = new BigDecimal("6.5");
    private static final int MICRO_FINANCE_TENURE = 3;
    private static final int MICRO_FINANCE_MORATORIUM = 3;

    // Term Loan Scheme constants
    private static final BigDecimal TERM_LOAN_MAX_FUNDING = new BigDecimal("4500000");
    private static final BigDecimal TERM_LOAN_INTEREST = new BigDecimal("8.0");
    private static final int TERM_LOAN_TENURE = 7;
    private static final int TERM_LOAN_MORATORIUM = 6;

    /**
     * Determines the applicable scheme based on project cost.
     *
     * @param projectCost The total project cost
     * @return SchemeDetailsDTO containing scheme information
     */
    public SchemeDetailsDTO determineScheme(BigDecimal projectCost) {
        if (projectCost == null) {
            throw new IllegalArgumentException("Project cost cannot be null");
        }

        if (projectCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Project cost must be positive");
        }

        if (projectCost.compareTo(MICRO_FINANCE_THRESHOLD) <= 0) {
            return buildMicroFinanceScheme();
        } else if (projectCost.compareTo(TERM_LOAN_THRESHOLD) <= 0) {
            return buildTermLoanScheme();
        } else {
            throw new IllegalArgumentException(
                "Project cost exceeds maximum threshold of ₹50 lakh. " +
                "Please contact the scheme administrator for larger projects."
            );
        }
    }

    private SchemeDetailsDTO buildMicroFinanceScheme() {
        return SchemeDetailsDTO.builder()
                .schemeName("Micro Finance Scheme")
                .schemeType("MICRO_FINANCE")
                .maxFunding(MICRO_FINANCE_MAX_FUNDING)
                .interestRate(MICRO_FINANCE_INTEREST)
                .tenureYears(MICRO_FINANCE_TENURE)
                .moratoriumMonths(MICRO_FINANCE_MORATORIUM)
                .description("Suitable for small-scale enterprises with project cost up to ₹1.40 lakh")
                .build();
    }

    private SchemeDetailsDTO buildTermLoanScheme() {
        return SchemeDetailsDTO.builder()
                .schemeName("Term Loan Scheme")
                .schemeType("TERM_LOAN")
                .maxFunding(TERM_LOAN_MAX_FUNDING)
                .interestRate(TERM_LOAN_INTEREST)
                .tenureYears(TERM_LOAN_TENURE)
                .moratoriumMonths(TERM_LOAN_MORATORIUM)
                .description("Suitable for medium-scale enterprises with project cost between ₹1.40 lakh and ₹50 lakh")
                .build();
    }
}
