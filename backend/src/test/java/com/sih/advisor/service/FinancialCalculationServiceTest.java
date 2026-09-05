package com.sih.advisor.service;

import com.sih.advisor.dto.FinancialBreakdownDTO;
import com.sih.advisor.dto.RepaymentScheduleDTO;
import com.sih.advisor.dto.SchemeDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FinancialCalculationService.
 */
@ExtendWith(MockitoExtension.class)
class FinancialCalculationServiceTest {

    @Mock
    private SchemeRouterService schemeRouterService;

    @Mock
    private EMICalculatorService emiCalculatorService;

    @InjectMocks
    private FinancialCalculationService financialCalculationService;

    private SchemeDetailsDTO microFinanceScheme;
    private SchemeDetailsDTO termLoanScheme;

    @BeforeEach
    void setUp() {
        microFinanceScheme = SchemeDetailsDTO.builder()
                .schemeName("Micro Finance Scheme")
                .schemeType("MICRO_FINANCE")
                .maxFunding(new BigDecimal("125000"))
                .interestRate(new BigDecimal("6.5"))
                .tenureYears(3)
                .moratoriumMonths(3)
                .build();

        termLoanScheme = SchemeDetailsDTO.builder()
                .schemeName("Term Loan Scheme")
                .schemeType("TERM_LOAN")
                .maxFunding(new BigDecimal("4500000"))
                .interestRate(new BigDecimal("8.0"))
                .tenureYears(7)
                .moratoriumMonths(6)
                .build();
    }

    @Test
    void testCalculateFinancialBreakdown_MicroFinanceScheme() {
        // Given: Available margin = ₹14,000
        // Expected: Project cost = ₹1,40,000, Max loan = ₹1,26,000
        // Scheme: Micro Finance (capped at ₹1,25,000)
        BigDecimal availableMargin = new BigDecimal("14000");
        BigDecimal expectedProjectCost = new BigDecimal("140000.00");
        BigDecimal expectedMaxLoan = new BigDecimal("126000.00");
        BigDecimal expectedActualLoan = new BigDecimal("125000"); // Capped
        BigDecimal expectedEMI = new BigDecimal("3831.13");

        when(schemeRouterService.determineScheme(expectedProjectCost))
                .thenReturn(microFinanceScheme);
        when(emiCalculatorService.calculateEMI(expectedActualLoan, new BigDecimal("6.5"), 3))
                .thenReturn(expectedEMI);
        when(emiCalculatorService.calculateTotalRepayment(expectedEMI, 3))
                .thenReturn(new BigDecimal("137920.68"));

        // When
        FinancialBreakdownDTO result = financialCalculationService.calculateFinancialBreakdown(availableMargin);

        // Then
        assertNotNull(result);
        assertEquals(availableMargin, result.getAvailableMargin());
        assertEquals(expectedProjectCost, result.getProjectCost());
        assertEquals(expectedMaxLoan, result.getMaxLoanAmount());
        assertEquals(expectedActualLoan, result.getActualLoanAmount());
        assertEquals("Micro Finance Scheme", result.getApplicableScheme().getSchemeName());
        assertEquals(expectedEMI, result.getEmiAmount());
        assertTrue(result.getMessage().contains("capped"));

        verify(schemeRouterService).determineScheme(expectedProjectCost);
        verify(emiCalculatorService).calculateEMI(expectedActualLoan, new BigDecimal("6.5"), 3);
    }

    @Test
    void testCalculateFinancialBreakdown_TermLoanScheme() {
        // Given: Available margin = ₹1,00,000
        // Expected: Project cost = ₹10,00,000, Max loan = ₹9,00,000
        // Scheme: Term Loan (no capping)
        BigDecimal availableMargin = new BigDecimal("100000");
        BigDecimal expectedProjectCost = new BigDecimal("1000000.00");
        BigDecimal expectedMaxLoan = new BigDecimal("900000.00");
        BigDecimal expectedEMI = new BigDecimal("14026.79");

        when(schemeRouterService.determineScheme(expectedProjectCost))
                .thenReturn(termLoanScheme);
        when(emiCalculatorService.calculateEMI(expectedMaxLoan, new BigDecimal("8.0"), 7))
                .thenReturn(expectedEMI);
        when(emiCalculatorService.calculateTotalRepayment(expectedEMI, 7))
                .thenReturn(new BigDecimal("1178250.48"));

        // When
        FinancialBreakdownDTO result = financialCalculationService.calculateFinancialBreakdown(availableMargin);

        // Then
        assertNotNull(result);
        assertEquals(expectedProjectCost, result.getProjectCost());
        assertEquals(expectedMaxLoan, result.getMaxLoanAmount());
        assertEquals(expectedMaxLoan, result.getActualLoanAmount()); // Not capped
        assertEquals("Term Loan Scheme", result.getApplicableScheme().getSchemeName());
        assertFalse(result.getMessage().contains("capped"));
    }

    @Test
    void testCalculateFinancialBreakdown_SmallMargin() {
        // Given: Available margin = ₹5,000
        BigDecimal availableMargin = new BigDecimal("5000");
        BigDecimal expectedProjectCost = new BigDecimal("50000.00");

        when(schemeRouterService.determineScheme(any())).thenReturn(microFinanceScheme);
        when(emiCalculatorService.calculateEMI(any(), any(), anyInt()))
                .thenReturn(new BigDecimal("1376.12"));
        when(emiCalculatorService.calculateTotalRepayment(any(), anyInt()))
                .thenReturn(new BigDecimal("49540.32"));

        // When
        FinancialBreakdownDTO result = financialCalculationService.calculateFinancialBreakdown(availableMargin);

        // Then
        assertNotNull(result);
        assertEquals(expectedProjectCost, result.getProjectCost());
        assertNotNull(result.getEstimatedWorkingCapital());
        assertNotNull(result.getEstimatedOperationalCost());
    }

    @Test
    void testGenerateRepaymentSchedule() {
        // Given
        BigDecimal loanAmount = new BigDecimal("100000");
        BigDecimal interestRate = new BigDecimal("6.5");
        int tenure = 3;
        int moratorium = 3;
        BigDecimal expectedEMI = new BigDecimal("3065.60");

        when(emiCalculatorService.calculateEMI(loanAmount, interestRate, tenure))
                .thenReturn(expectedEMI);
        when(emiCalculatorService.calculateTotalRepayment(expectedEMI, tenure))
                .thenReturn(new BigDecimal("110361.60"));
        when(emiCalculatorService.generateRepaymentSchedule(loanAmount, interestRate, tenure, moratorium))
                .thenReturn(new ArrayList<>());

        // When
        RepaymentScheduleDTO result = financialCalculationService.generateRepaymentSchedule(
                loanAmount, interestRate, tenure, moratorium
        );

        // Then
        assertNotNull(result);
        assertEquals(loanAmount, result.getLoanAmount());
        assertEquals(interestRate, result.getInterestRate());
        assertEquals(tenure, result.getTenureYears());
        assertEquals(moratorium, result.getMoratoriumMonths());
        assertEquals(expectedEMI, result.getEmiAmount());

        verify(emiCalculatorService).calculateEMI(loanAmount, interestRate, tenure);
        verify(emiCalculatorService).generateRepaymentSchedule(loanAmount, interestRate, tenure, moratorium);
    }
}
