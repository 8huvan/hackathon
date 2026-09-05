package com.sih.advisor.dto;

import java.math.BigDecimal;

public class SchemeDetailsDTO {

    private String schemeName;
    private String schemeType;
    private BigDecimal maxFunding;
    private BigDecimal interestRate;
    private Integer tenureYears;
    private Integer moratoriumMonths;
    private String description;

    public SchemeDetailsDTO() {
    }

    public SchemeDetailsDTO(String schemeName, String schemeType, BigDecimal maxFunding,
                           BigDecimal interestRate, Integer tenureYears, Integer moratoriumMonths,
                           String description) {
        this.schemeName = schemeName;
        this.schemeType = schemeType;
        this.maxFunding = maxFunding;
        this.interestRate = interestRate;
        this.tenureYears = tenureYears;
        this.moratoriumMonths = moratoriumMonths;
        this.description = description;
    }

    public static SchemeDetailsDTOBuilder builder() {
        return new SchemeDetailsDTOBuilder();
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }

    public BigDecimal getMaxFunding() {
        return maxFunding;
    }

    public void setMaxFunding(BigDecimal maxFunding) {
        this.maxFunding = maxFunding;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class SchemeDetailsDTOBuilder {
        private String schemeName;
        private String schemeType;
        private BigDecimal maxFunding;
        private BigDecimal interestRate;
        private Integer tenureYears;
        private Integer moratoriumMonths;
        private String description;

        public SchemeDetailsDTOBuilder schemeName(String schemeName) {
            this.schemeName = schemeName;
            return this;
        }

        public SchemeDetailsDTOBuilder schemeType(String schemeType) {
            this.schemeType = schemeType;
            return this;
        }

        public SchemeDetailsDTOBuilder maxFunding(BigDecimal maxFunding) {
            this.maxFunding = maxFunding;
            return this;
        }

        public SchemeDetailsDTOBuilder interestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public SchemeDetailsDTOBuilder tenureYears(Integer tenureYears) {
            this.tenureYears = tenureYears;
            return this;
        }

        public SchemeDetailsDTOBuilder moratoriumMonths(Integer moratoriumMonths) {
            this.moratoriumMonths = moratoriumMonths;
            return this;
        }

        public SchemeDetailsDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SchemeDetailsDTO build() {
            return new SchemeDetailsDTO(schemeName, schemeType, maxFunding, interestRate,
                                       tenureYears, moratoriumMonths, description);
        }
    }
}
