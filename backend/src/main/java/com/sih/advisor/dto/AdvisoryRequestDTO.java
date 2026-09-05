package com.sih.advisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for business advisory request input.
 * Contains location details, business category, and available capital.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvisoryRequestDTO {

    @NotBlank(message = "Village name is required")
    private String village;

    @NotBlank(message = "Block name is required")
    private String block;

    @NotBlank(message = "District name is required")
    private String district;

    @NotBlank(message = "State name is required")
    private String state;

    @NotBlank(message = "Business category is required")
    private String businessCategory;

    @NotNull(message = "Available margin capital is required")
    @Positive(message = "Available margin must be positive")
    private BigDecimal availableMargin;
}
