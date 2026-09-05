package com.sih.advisor.service;

import com.sih.advisor.dto.RepaymentInstallmentDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for calculating EMI (Equated Monthly Installment) and generating
 * repayment schedules using the reducing balance method.
 */
@Service
public class EMICalculatorService {

    private static final int MONTHS_PER_YEAR = 12;
    private static final int MONTHS_PER_QUARTER = 3;
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final int CALCULATION_SCALE = 10;
    private static final int DISPLAY_SCALE = 2;

    /**
     * Calculates monthly EMI using the reducing balance formula:
     * EMI = [P × R × (1+R)^N] / [(1+R)^N - 1]
     * where P = principal, R = monthly rate, N = number of months
     *
     * @param principal Loan amount
     * @param annualInterestRate Annual interest rate (e.g., 6.5 for 6.5%)
     * @param tenureYears Loan tenure in years
     * @return Monthly EMI amount
     */
    public BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualInterestRate, int tenureYears) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive");
        }
        if (annualInterestRate == null || annualInterestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        if (tenureYears <= 0) {
            throw new IllegalArgumentException("Tenure must be positive");
        }

        // Convert annual rate to monthly rate (as decimal)
        BigDecimal monthlyRate = annualInterestRate
                .divide(HUNDRED, CALCULATION_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(MONTHS_PER_YEAR), CALCULATION_SCALE, RoundingMode.HALF_UP);

        int totalMonths = tenureYears * MONTHS_PER_YEAR;

        // If interest rate is 0, EMI is simply principal / months
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(totalMonths), DISPLAY_SCALE, RoundingMode.HALF_UP);
        }

        // Calculate (1 + R)^N
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal powerTerm = onePlusRate.pow(totalMonths);

        // EMI = [P × R × (1+R)^N] / [(1+R)^N - 1]
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(powerTerm);
        BigDecimal denominator = powerTerm.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, DISPLAY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Generates a quarterly repayment schedule after moratorium period.
     * During moratorium, only interest is paid (if applicable).
     *
     * @param principal Loan amount
     * @param annualInterestRate Annual interest rate
     * @param tenureYears Loan tenure in years
     * @param moratoriumMonths Moratorium period in months
     * @return List of quarterly installment details
     */
    public List<RepaymentInstallmentDTO> generateRepaymentSchedule(
            BigDecimal principal,
            BigDecimal annualInterestRate,
            int tenureYears,
            int moratoriumMonths) {

        BigDecimal monthlyEMI = calculateEMI(principal, annualInterestRate, tenureYears);
        BigDecimal monthlyRate = annualInterestRate
                .divide(HUNDRED, CALCULATION_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(MONTHS_PER_YEAR), CALCULATION_SCALE, RoundingMode.HALF_UP);

        List<RepaymentInstallmentDTO> schedule = new ArrayList<>();
        BigDecimal outstandingBalance = principal;
        int totalMonths = tenureYears * MONTHS_PER_YEAR;
        int installmentNumber = 1;

        // Generate monthly schedule first, then aggregate quarterly
        List<MonthlyPayment> monthlyPayments = new ArrayList<>();

        for (int month = 1; month <= totalMonths; month++) {
            BigDecimal interestForMonth = outstandingBalance.multiply(monthlyRate)
                    .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP);
            BigDecimal principalForMonth;
            BigDecimal totalPayment;

            if (month <= moratoriumMonths) {
                // During moratorium: only interest is paid (no principal reduction)
                principalForMonth = BigDecimal.ZERO;
                totalPayment = interestForMonth;
            } else {
                // After moratorium: regular EMI
                principalForMonth = monthlyEMI.subtract(interestForMonth);
                totalPayment = monthlyEMI;
                outstandingBalance = outstandingBalance.subtract(principalForMonth)
                        .max(BigDecimal.ZERO);
            }

            monthlyPayments.add(new MonthlyPayment(principalForMonth, interestForMonth, totalPayment, outstandingBalance));
        }

        // Aggregate into quarterly installments
        for (int i = 0; i < monthlyPayments.size(); i += MONTHS_PER_QUARTER) {
            BigDecimal quarterlyPrincipal = BigDecimal.ZERO;
            BigDecimal quarterlyInterest = BigDecimal.ZERO;
            BigDecimal quarterlyTotal = BigDecimal.ZERO;
            BigDecimal endBalance = BigDecimal.ZERO;

            int monthsInQuarter = Math.min(MONTHS_PER_QUARTER, monthlyPayments.size() - i);
            for (int j = 0; j < monthsInQuarter; j++) {
                MonthlyPayment mp = monthlyPayments.get(i + j);
                quarterlyPrincipal = quarterlyPrincipal.add(mp.principal);
                quarterlyInterest = quarterlyInterest.add(mp.interest);
                quarterlyTotal = quarterlyTotal.add(mp.total);
                endBalance = mp.balance;
            }

            int quarter = (i / MONTHS_PER_QUARTER) % 4 + 1;
            int year = (i / MONTHS_PER_QUARTER) / 4 + 1;

            schedule.add(RepaymentInstallmentDTO.builder()
                    .installmentNumber(installmentNumber++)
                    .quarter(quarter)
                    .year(year)
                    .principalAmount(quarterlyPrincipal.setScale(DISPLAY_SCALE, RoundingMode.HALF_UP))
                    .interestAmount(quarterlyInterest.setScale(DISPLAY_SCALE, RoundingMode.HALF_UP))
                    .totalPayment(quarterlyTotal.setScale(DISPLAY_SCALE, RoundingMode.HALF_UP))
                    .outstandingBalance(endBalance.setScale(DISPLAY_SCALE, RoundingMode.HALF_UP))
                    .build());
        }

        return schedule;
    }

    /**
     * Calculates total repayment amount (principal + interest).
     */
    public BigDecimal calculateTotalRepayment(BigDecimal emi, int tenureYears) {
        int totalMonths = tenureYears * MONTHS_PER_YEAR;
        return emi.multiply(new BigDecimal(totalMonths))
                .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP);
    }

    // Helper class for monthly payment tracking
    private static class MonthlyPayment {
        BigDecimal principal;
        BigDecimal interest;
        BigDecimal total;
        BigDecimal balance;

        MonthlyPayment(BigDecimal principal, BigDecimal interest, BigDecimal total, BigDecimal balance) {
            this.principal = principal;
            this.interest = interest;
            this.total = total;
            this.balance = balance;
        }
    }
}
