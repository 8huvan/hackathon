package com.sih.advisor.dto;

import java.math.BigDecimal;
import java.util.List;

public class RepaymentScheduleDTO {

    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer tenureYears;
    private Integer moratoriumMonths;
    private BigDecimal emiAmount;
    private BigDecimal totalRepayment;
    private BigDecimal totalInterest;
    private Integer totalInstallments;
    private List<RepaymentInstallmentDTO> schedule;

    public RepaymentScheduleDTO() {
    }

    public RepaymentScheduleDTO(BigDecimal loanAmount, BigDecimal interestRate, Integer tenureYears,
                               Integer moratoriumMonths, BigDecimal emiAmount, BigDecimal totalRepayment,
                               BigDecimal totalInterest, Integer totalInstallments,
                               List<RepaymentInstallmentDTO> schedule) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.tenureYears = tenureYears;
        this.moratoriumMonths = moratoriumMonths;
        this.emiAmount = emiAmount;
        this.totalRepayment = totalRepayment;
        this.totalInterest = totalInterest;
        this.totalInstallments = totalInstallments;
        this.schedule = schedule;
    }

    public static RepaymentScheduleDTOBuilder builder() {
        return new RepaymentScheduleDTOBuilder();
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTenureYears() {
        return tenureYears;
    }

    public void setTenureYears(Integer tenureYears) {
        this.tenureYears = tenureYears;
    }

    public Integer getMoratoriumMonths() {
        return moratoriumMonths;
    }

    public void setMoratoriumMonths(Integer moratoriumMonths) {
        this.moratoriumMonths = moratoriumMonths;
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

    public List<RepaymentInstallmentDTO> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<RepaymentInstallmentDTO> schedule) {
        this.schedule = schedule;
    }

    public static class RepaymentScheduleDTOBuilder {
        private BigDecimal loanAmount;
        private BigDecimal interestRate;
        private Integer tenureYears;
        private Integer moratoriumMonths;
        private BigDecimal emiAmount;
        private BigDecimal totalRepayment;
        private BigDecimal totalInterest;
        private Integer totalInstallments;
        private List<RepaymentInstallmentDTO> schedule;

        public RepaymentScheduleDTOBuilder loanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
            return this;
        }

        public RepaymentScheduleDTOBuilder interestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public RepaymentScheduleDTOBuilder tenureYears(Integer tenureYears) {
            this.tenureYears = tenureYears;
            return this;
        }

        public RepaymentScheduleDTOBuilder moratoriumMonths(Integer moratoriumMonths) {
            this.moratoriumMonths = moratoriumMonths;
            return this;
        }

        public RepaymentScheduleDTOBuilder emiAmount(BigDecimal emiAmount) {
            this.emiAmount = emiAmount;
            return this;
        }

        public RepaymentScheduleDTOBuilder totalRepayment(BigDecimal totalRepayment) {
            this.totalRepayment = totalRepayment;
            return this;
        }

        public RepaymentScheduleDTOBuilder totalInterest(BigDecimal totalInterest) {
            this.totalInterest = totalInterest;
            return this;
        }

        public RepaymentScheduleDTOBuilder totalInstallments(Integer totalInstallments) {
            this.totalInstallments = totalInstallments;
            return this;
        }

        public RepaymentScheduleDTOBuilder schedule(List<RepaymentInstallmentDTO> schedule) {
            this.schedule = schedule;
            return this;
        }

        public RepaymentScheduleDTO build() {
            return new RepaymentScheduleDTO(loanAmount, interestRate, tenureYears, moratoriumMonths,
                                           emiAmount, totalRepayment, totalInterest, totalInstallments, schedule);
        }
    }
}
