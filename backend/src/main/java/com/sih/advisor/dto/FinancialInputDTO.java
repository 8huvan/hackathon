package com.sih.advisor.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class FinancialInputDTO {

    @NotNull(message = "Available margin capital is required")
    @DecimalMin(value = "1000.0", message = "Available margin must be at least ₹1,000")
    private BigDecimal availableMargin;

    public FinancialInputDTO() {
    }

    public FinancialInputDTO(BigDecimal availableMargin) {
        this.availableMargin = availableMargin;
    }

    public BigDecimal getAvailableMargin() {
        return availableMargin;
    }

    public void setAvailableMargin(BigDecimal availableMargin) {
        this.availableMargin = availableMargin;
    }
}
