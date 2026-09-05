package com.sih.advisor.service;

import com.sih.advisor.dto.RepaymentInstallmentDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EMICalculatorService.
 * Tests the deterministic EMI and repayment schedule calculations.
 */
class EMICalculatorServiceTest {

    private final EMICalculatorService emiCalculatorService = new EMICalculatorService();

    @Test
    void testCalculateEMI_MicroFinanceScheme() {
        // Micro Finance: ₹1,25,000 at 6.5% for 3 years
        BigDecimal principal = new BigDecimal("125000");
        BigDecimal interestRate = new BigDecimal("6.5");
        int tenure = 3;

        BigDecimal emi = emiCalculatorService.calculateEMI(principal, interestRate, tenure);

        assertNotNull(emi);
        assertTrue(emi.compareTo(BigDecimal.ZERO) > 0);
        // Expected EMI ≈ ₹3,831.13 (verified with EMI formula)
        assertEquals(0, emi.compareTo(new BigDecimal("3831.13")),
                "EMI calculation mismatch. Expected: ₹3,831.13, Got: ₹" + emi);
    }

    @Test
    void testCalculateEMI_TermLoanScheme() {
        // Term Loan: ₹10,00,000 at 8% for 7 years
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal interestRate = new BigDecimal("8.0");
        int tenure = 7;

        BigDecimal emi = emiCalculatorService.calculateEMI(principal, interestRate, tenure);

        assertNotNull(emi);
        assertTrue(emi.compareTo(BigDecimal.ZERO) > 0);
        // Expected EMI ≈ ₹15,585.83
        assertTrue(emi.compareTo(new BigDecimal("15500")) > 0);
        assertTrue(emi.compareTo(new BigDecimal("16000")) < 0);
    }

    @Test
    void testCalculateEMI_ZeroInterest() {
        // Edge case: 0% interest
        BigDecimal principal = new BigDecimal("120000");
        BigDecimal interestRate = BigDecimal.ZERO;
        int tenure = 3;

        BigDecimal emi = emiCalculatorService.calculateEMI(principal, interestRate, tenure);

        // With 0% interest, EMI = Principal / Months
        BigDecimal expected = principal.divide(new BigDecimal(36), 2, RoundingMode.HALF_UP);
        assertEquals(0, emi.compareTo(expected));
    }

    @Test
    void testCalculateEMI_SmallLoan() {
        BigDecimal principal = new BigDecimal("10000");
        BigDecimal interestRate = new BigDecimal("5.0");
        int tenure = 1;

        BigDecimal emi = emiCalculatorService.calculateEMI(principal, interestRate, tenure);

        assertNotNull(emi);
        assertTrue(emi.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateEMI_NullPrincipal() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emiCalculatorService.calculateEMI(null, new BigDecimal("6.5"), 3)
        );
    }

    @Test
    void testCalculateEMI_NegativePrincipal() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emiCalculatorService.calculateEMI(new BigDecimal("-1000"), new BigDecimal("6.5"), 3)
        );
    }

    @Test
    void testCalculateEMI_NegativeInterestRate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emiCalculatorService.calculateEMI(new BigDecimal("100000"), new BigDecimal("-5"), 3)
        );
    }

    @Test
    void testCalculateEMI_ZeroTenure() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emiCalculatorService.calculateEMI(new BigDecimal("100000"), new BigDecimal("6.5"), 0)
        );
    }

    @Test
    void testCalculateTotalRepayment() {
        BigDecimal emi = new BigDecimal("3831.13");
        int tenure = 3;

        BigDecimal totalRepayment = emiCalculatorService.calculateTotalRepayment(emi, tenure);

        // 3 years = 36 months
        BigDecimal expected = emi.multiply(new BigDecimal(36)).setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, totalRepayment.compareTo(expected));
    }

    @Test
    void testGenerateRepaymentSchedule_NoMoratorium() {
        BigDecimal principal = new BigDecimal("100000");
        BigDecimal interestRate = new BigDecimal("6.0");
        int tenure = 2;
        int moratorium = 0;

        List<RepaymentInstallmentDTO> schedule = emiCalculatorService.generateRepaymentSchedule(
                principal, interestRate, tenure, moratorium
        );

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());

        // 2 years = 8 quarters
        assertEquals(8, schedule.size());

        // Verify first installment
        RepaymentInstallmentDTO firstInstallment = schedule.get(0);
        assertEquals(1, firstInstallment.getInstallmentNumber());
        assertEquals(1, firstInstallment.getQuarter());
        assertEquals(1, firstInstallment.getYear());
        assertNotNull(firstInstallment.getPrincipalAmount());
        assertNotNull(firstInstallment.getInterestAmount());
        assertNotNull(firstInstallment.getTotalPayment());
        assertNotNull(firstInstallment.getOutstandingBalance());

        // Outstanding balance should decrease over time
        assertTrue(schedule.get(0).getOutstandingBalance()
                .compareTo(schedule.get(7).getOutstandingBalance()) > 0);

        // Last installment should have near-zero balance
        BigDecimal lastBalance = schedule.get(7).getOutstandingBalance();
        assertTrue(lastBalance.compareTo(new BigDecimal("100")) < 0);
    }

    @Test
    void testGenerateRepaymentSchedule_WithMoratorium() {
        BigDecimal principal = new BigDecimal("125000");
        BigDecimal interestRate = new BigDecimal("6.5");
        int tenure = 3;
        int moratorium = 3; // 3 months moratorium

        List<RepaymentInstallmentDTO> schedule = emiCalculatorService.generateRepaymentSchedule(
                principal, interestRate, tenure, moratorium
        );

        assertNotNull(schedule);
        assertEquals(12, schedule.size()); // 3 years = 12 quarters

        // During moratorium (first quarter), principal should be zero
        RepaymentInstallmentDTO firstQuarter = schedule.get(0);
        // First quarter includes the 3-month moratorium period
        // During moratorium, only interest is paid, no principal
        assertNotNull(firstQuarter.getPrincipalAmount());
        assertNotNull(firstQuarter.getInterestAmount());

        // Outstanding balance should start decreasing after moratorium
        assertTrue(schedule.get(1).getOutstandingBalance()
                .compareTo(schedule.get(11).getOutstandingBalance()) > 0);
    }

    @Test
    void testGenerateRepaymentSchedule_QuarterlyAggregation() {
        BigDecimal principal = new BigDecimal("120000");
        BigDecimal interestRate = new BigDecimal("7.0");
        int tenure = 1; // 1 year = 4 quarters
        int moratorium = 0;

        List<RepaymentInstallmentDTO> schedule = emiCalculatorService.generateRepaymentSchedule(
                principal, interestRate, tenure, moratorium
        );

        assertEquals(4, schedule.size());

        // Verify quarter numbering
        for (int i = 0; i < 4; i++) {
            RepaymentInstallmentDTO installment = schedule.get(i);
            assertEquals(i + 1, installment.getInstallmentNumber());
            assertEquals(i + 1, installment.getQuarter());
            assertEquals(1, installment.getYear());
        }
    }

    @Test
    void testGenerateRepaymentSchedule_MultiYear() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal interestRate = new BigDecimal("8.0");
        int tenure = 7; // 7 years
        int moratorium = 6;

        List<RepaymentInstallmentDTO> schedule = emiCalculatorService.generateRepaymentSchedule(
                principal, interestRate, tenure, moratorium
        );

        assertEquals(28, schedule.size()); // 7 years = 28 quarters

        // Verify year transitions
        RepaymentInstallmentDTO q4y1 = schedule.get(3);
        assertEquals(4, q4y1.getQuarter());
        assertEquals(1, q4y1.getYear());

        RepaymentInstallmentDTO q1y2 = schedule.get(4);
        assertEquals(1, q1y2.getQuarter());
        assertEquals(2, q1y2.getYear());

        RepaymentInstallmentDTO lastQuarter = schedule.get(27);
        assertEquals(4, lastQuarter.getQuarter());
        assertEquals(7, lastQuarter.getYear());
    }
}
