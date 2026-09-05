package com.sih.advisor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entity representing a business category with its characteristics and requirements.
 * Used for business advisory and feasibility analysis.
 */
@Entity
@Table(name = "business_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String category;

    @Column(nullable = false, length = 100)
    private String businessType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal minInvestment;

    @Column(nullable = false)
    private BigDecimal maxInvestment;

    @Column(columnDefinition = "TEXT")
    private String requiredResources;

    @Column(columnDefinition = "TEXT")
    private String targetCustomers;

    @Column(columnDefinition = "TEXT")
    private String distributionConsiderations;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 50)
    private String suitableFor; // e.g., "rural", "urban", "both"

    @Column
    private Integer typicalMarketRadiusKm; // Typical market reach in kilometers
}
