package com.sih.advisor.dto;

import java.math.BigDecimal;

public class RepaymentInstallmentDTO {

    private Integer installmentNumber;
    private Integer quarter;
    private Integer year;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPayment;
    private BigDecimal outstandingBalance;

    public RepaymentInstallmentDTO() {
    }

    public RepaymentInstallmentDTO(Integer installmentNumber, Integer quarter, Integer year,
                                  BigDecimal principalAmount, BigDecimal interestAmount,
                                  BigDecimal totalPayment, BigDecimal outstandingBalance) {
        this.installmentNumber = installmentNumber;
        this.quarter = quarter;
        this.year = year;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.totalPayment = totalPayment;
        this.outstandingBalance = outstandingBalance;
    }

    public static RepaymentInstallmentDTOBuilder builder() {
        return new RepaymentInstallmentDTOBuilder();
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public static class RepaymentInstallmentDTOBuilder {
        private Integer installmentNumber;
        private Integer quarter;
        private Integer year;
        private BigDecimal principalAmount;
        private BigDecimal interestAmount;
        private BigDecimal totalPayment;
        private BigDecimal outstandingBalance;

        public RepaymentInstallmentDTOBuilder installmentNumber(Integer installmentNumber) {
            this.installmentNumber = installmentNumber;
            return this;
        }

        public RepaymentInstallmentDTOBuilder quarter(Integer quarter) {
            this.quarter = quarter;
            return this;
        }

        public RepaymentInstallmentDTOBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public RepaymentInstallmentDTOBuilder principalAmount(BigDecimal principalAmount) {
            this.principalAmount = principalAmount;
            return this;
        }

        public RepaymentInstallmentDTOBuilder interestAmount(BigDecimal interestAmount) {
            this.interestAmount = interestAmount;
            return this;
        }

        public RepaymentInstallmentDTOBuilder totalPayment(BigDecimal totalPayment) {
            this.totalPayment = totalPayment;
            return this;
        }

        public RepaymentInstallmentDTOBuilder outstandingBalance(BigDecimal outstandingBalance) {
            this.outstandingBalance = outstandingBalance;
            return this;
        }

        public RepaymentInstallmentDTO build() {
            return new RepaymentInstallmentDTO(installmentNumber, quarter, year, principalAmount,
                                              interestAmount, totalPayment, outstandingBalance);
        }
    }
}
