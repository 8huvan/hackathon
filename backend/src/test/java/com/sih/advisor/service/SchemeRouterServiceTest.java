package com.sih.advisor.service;

import com.sih.advisor.dto.SchemeDetailsDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SchemeRouterService.
 * Tests the deterministic scheme routing logic.
 */
class SchemeRouterServiceTest {

    private final SchemeRouterService schemeRouterService = new SchemeRouterService();

    @Test
    void testMicroFinanceScheme_ExactThreshold() {
        BigDecimal projectCost = new BigDecimal("140000");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertNotNull(scheme);
        assertEquals("Micro Finance Scheme", scheme.getSchemeName());
        assertEquals("MICRO_FINANCE", scheme.getSchemeType());
        assertEquals(new BigDecimal("125000"), scheme.getMaxFunding());
        assertEquals(new BigDecimal("6.5"), scheme.getInterestRate());
        assertEquals(3, scheme.getTenureYears());
        assertEquals(3, scheme.getMoratoriumMonths());
    }

    @Test
    void testMicroFinanceScheme_BelowThreshold() {
        BigDecimal projectCost = new BigDecimal("100000");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertEquals("Micro Finance Scheme", scheme.getSchemeName());
        assertEquals("MICRO_FINANCE", scheme.getSchemeType());
    }

    @Test
    void testMicroFinanceScheme_VerySmallAmount() {
        BigDecimal projectCost = new BigDecimal("10000");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertEquals("Micro Finance Scheme", scheme.getSchemeName());
    }

    @Test
    void testTermLoanScheme_JustAboveThreshold() {
        BigDecimal projectCost = new BigDecimal("140001");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertNotNull(scheme);
        assertEquals("Term Loan Scheme", scheme.getSchemeName());
        assertEquals("TERM_LOAN", scheme.getSchemeType());
        assertEquals(new BigDecimal("4500000"), scheme.getMaxFunding());
        assertEquals(new BigDecimal("8.0"), scheme.getInterestRate());
        assertEquals(7, scheme.getTenureYears());
        assertEquals(6, scheme.getMoratoriumMonths());
    }

    @Test
    void testTermLoanScheme_MidRange() {
        BigDecimal projectCost = new BigDecimal("2500000");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertEquals("Term Loan Scheme", scheme.getSchemeName());
        assertEquals("TERM_LOAN", scheme.getSchemeType());
    }

    @Test
    void testTermLoanScheme_MaxThreshold() {
        BigDecimal projectCost = new BigDecimal("5000000");
        SchemeDetailsDTO scheme = schemeRouterService.determineScheme(projectCost);

        assertEquals("Term Loan Scheme", scheme.getSchemeName());
    }

    @Test
    void testProjectCostExceedsMaximum() {
        BigDecimal projectCost = new BigDecimal("5000001");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> schemeRouterService.determineScheme(projectCost)
        );

        assertTrue(exception.getMessage().contains("exceeds maximum threshold"));
    }

    @Test
    void testNullProjectCost() {
        assertThrows(
                IllegalArgumentException.class,
                () -> schemeRouterService.determineScheme(null)
        );
    }

    @Test
    void testZeroProjectCost() {
        BigDecimal projectCost = BigDecimal.ZERO;

        assertThrows(
                IllegalArgumentException.class,
                () -> schemeRouterService.determineScheme(projectCost)
        );
    }

    @Test
    void testNegativeProjectCost() {
        BigDecimal projectCost = new BigDecimal("-1000");

        assertThrows(
                IllegalArgumentException.class,
                () -> schemeRouterService.determineScheme(projectCost)
        );
    }
}
