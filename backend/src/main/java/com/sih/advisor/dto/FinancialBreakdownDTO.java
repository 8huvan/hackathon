package com.sih.advisor.dto;

import java.math.BigDecimal;

public class FinancialBreakdownDTO {

    private BigDecimal availableMargin;
    private BigDecimal projectCost;
    private BigDecimal maxLoanAmount;
    private BigDecimal actualLoanAmount;
    private SchemeDetailsDTO applicableScheme;
    private BigDecimal emiAmount;
    private BigDecimal totalRepayment;
    private BigDecimal totalInterest;
    private Integer totalInstallments;
    private BigDecimal estimatedWorkingCapital;
    private BigDecimal estimatedOperationalCost;
    private String message;

    public FinancialBreakdownDTO() {
    }

    public FinancialBreakdownDTO(BigDecimal availableMargin, BigDecimal projectCost, BigDecimal maxLoanAmount,
                                BigDecimal actualLoanAmount, SchemeDetailsDTO applicableScheme, BigDecimal emiAmount,
                                BigDecimal totalRepayment, BigDecimal totalInterest, Integer totalInstallments,
                                BigDecimal estimatedWorkingCapital, BigDecimal estimatedOperationalCost, String message) {
        this.availableMargin = availableMargin;
        this.projectCost = projectCost;
        this.maxLoanAmount = maxLoanAmount;
        this.actualLoanAmount = actualLoanAmount;
        this.applicableScheme = applicableScheme;
        this.emiAmount = emiAmount;
        this.totalRepayment = totalRepayment;
        this.totalInterest = totalInterest;
        this.totalInstallments = totalInstallments;
        this.estimatedWorkingCapital = estimatedWorkingCapital;
        this.estimatedOperationalCost = estimatedOperationalCost;
        this.message = message;
    }

    public static FinancialBreakdownDTOBuilder builder() {
        return new FinancialBreakdownDTOBuilder();
    }

    public BigDecimal getAvailableMargin() {
        return availableMargin;
    }

    public void setAvailableMargin(BigDecimal availableMargin) {
        this.availableMargin = availableMargin;
    }

    public BigDecimal getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(BigDecimal projectCost) {
        this.projectCost = projectCost;
    }

    public BigDecimal getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public BigDecimal getActualLoanAmount() {
        return actualLoanAmount;
    }

    public void setActualLoanAmount(BigDecimal actualLoanAmount) {
        this.actualLoanAmount = actualLoanAmount;
    }

    public SchemeDetailsDTO getApplicableScheme() {
        return applicableScheme;
    }

    public void setApplicableScheme(SchemeDetailsDTO applicableScheme) {
        this.applicableScheme = applicableScheme;
    }

    public BigDecimal getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Integer getTotalInstallments() {
        return totalInstallments;
    }

    public void setTotalInstallments(Integer totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public BigDecimal getEstimatedWorkingCapital() {
        return estimatedWorkingCapital;
    }

    public void setEstimatedWorkingCapital(BigDecimal estimatedWorkingCapital) {
        this.estimatedWorkingCapital = estimatedWorkingCapital;
    }

    public BigDecimal getEstimatedOperationalCost() {
        return estimatedOperationalCost;
    }

    public void setEstimatedOperationalCost(BigDecimal estimatedOperationalCost) {
        this.estimatedOperationalCost = estimatedOperationalCost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class FinancialBreakdownDTOBuilder {
        private BigDecimal availableMargin;
        private BigDecimal projectCost;
        private BigDecimal maxLoanAmount;
        private BigDecimal actualLoanAmount;
        private SchemeDetailsDTO applicableScheme;
        private BigDecimal emiAmount;
        private BigDecimal totalRepayment;
        private BigDecimal totalInterest;
        private Integer totalInstallments;
        private BigDecimal estimatedWorkingCapital;
        private BigDecimal estimatedOperationalCost;
        private String message;

        public FinancialBreakdownDTOBuilder availableMargin(BigDecimal availableMargin) {
            this.availableMargin = availableMargin;
            return this;
        }

        public FinancialBreakdownDTOBuilder projectCost(BigDecimal projectCost) {
            this.projectCost = projectCost;
            return this;
        }

        public FinancialBreakdownDTOBuilder maxLoanAmount(BigDecimal maxLoanAmount) {
            this.maxLoanAmount = maxLoanAmount;
            return this;
        }

        public FinancialBreakdownDTOBuilder actualLoanAmount(BigDecimal actualLoanAmount) {
            this.actualLoanAmount = actualLoanAmount;
            return this;
        }

        public FinancialBreakdownDTOBuilder applicableScheme(SchemeDetailsDTO applicableScheme) {
            this.applicableScheme = applicableScheme;
            return this;
        }

        public FinancialBreakdownDTOBuilder emiAmount(BigDecimal emiAmount) {
            this.emiAmount = emiAmount;
            return this;
        }

        public FinancialBreakdownDTOBuilder totalRepayment(BigDecimal totalRepayment) {
            this.totalRepayment = totalRepayment;
            return this;
        }

        public FinancialBreakdownDTOBuilder totalInterest(BigDecimal totalInterest) {
            this.totalInterest = totalInterest;
            return this;
        }

        public FinancialBreakdownDTOBuilder totalInstallments(Integer totalInstallments) {
            this.totalInstallments = totalInstallments;
            return this;
        }

        public FinancialBreakdownDTOBuilder estimatedWorkingCapital(BigDecimal estimatedWorkingCapital) {
            this.estimatedWorkingCapital = estimatedWorkingCapital;
            return this;
        }

        public FinancialBreakdownDTOBuilder estimatedOperationalCost(BigDecimal estimatedOperationalCost) {
            this.estimatedOperationalCost = estimatedOperationalCost;
            return this;
        }

        public FinancialBreakdownDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public FinancialBreakdownDTO build() {
            return new FinancialBreakdownDTO(availableMargin, projectCost, maxLoanAmount, actualLoanAmount,
                                            applicableScheme, emiAmount, totalRepayment, totalInterest,
                                            totalInstallments, estimatedWorkingCapital, estimatedOperationalCost, message);
        }
    }
}
