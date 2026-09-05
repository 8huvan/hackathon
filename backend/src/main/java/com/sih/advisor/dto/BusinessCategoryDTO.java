package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for business category information.
 * Used to transfer business category data without exposing JPA entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCategoryDTO {

    private Long id;
    private String category;
    private String businessType;
    private String description;
    private BigDecimal minInvestment;
    private BigDecimal maxInvestment;
    private String requiredResources;
    private String targetCustomers;
    private String distributionConsiderations;
    private String suitableFor;
    private Integer typicalMarketRadiusKm;
}
