package com.sih.advisor.service;

import com.sih.advisor.dto.LocationDTO;
import com.sih.advisor.dto.MarketAnalysisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Mock implementation of LocationMarketService.
 * Generates deterministic, realistic market data based on location and business category.
 *
 * This implementation does NOT call external APIs or use real geographic data.
 * It provides sensible mock data for testing and demonstration purposes.
 */
@Service
@Slf4j
public class MockLocationMarketService implements LocationMarketService {

    @Override
    public MarketAnalysisDTO analyzeLocalMarket(LocationDTO location, String businessCategory) {
        log.info("Analyzing local market for {} in {}, {}",
                businessCategory, location.getVillage(), location.getDistrict());

        Integer consumerBase = estimateConsumerBase(location);
        Integer marketRadius = determineMarketRadius(businessCategory);

        return MarketAnalysisDTO.builder()
                .estimatedConsumerBase(consumerBase)
                .marketReachKm(marketRadius)
                .distributionChannels(determineDistributionChannels(businessCategory))
                .underservedNiches(identifyUnderservedNiches(businessCategory))
                .localOpportunities(identifyLocalOpportunities(businessCategory, location))
                .localThreats(identifyLocalThreats(businessCategory))
                .pricingGuidance(determinePricingGuidance(businessCategory))
                .averageLocalIncome(estimateAverageIncome(location))
                .marketDemandLevel(assessDemandLevel(businessCategory))
                .competitionLevel(assessCompetitionLevel(businessCategory))
                .build();
    }

    @Override
    public Integer estimateConsumerBase(LocationDTO location) {
        // Mock estimation based on location hierarchy
        // Rural villages typically have 2000-8000 people
        int basePopulation = 5000;
        int villageHash = Math.abs(location.getVillage().hashCode() % 3000);
        return basePopulation + villageHash;
    }

    @Override
    public Integer determineMarketRadius(String businessCategory) {
        // Different business types have different natural market radii
        String categoryLower = businessCategory.toLowerCase();

        if (categoryLower.contains("grocery") || categoryLower.contains("daily needs")) {
            return 3; // Very local, daily purchase items
        } else if (categoryLower.contains("hardware") || categoryLower.contains("electronics")) {
            return 8; // Occasional purchase, wider radius
        } else if (categoryLower.contains("tailoring") || categoryLower.contains("salon")) {
            return 5; // Service business, moderate radius
        } else {
            return 7; // Default hyper-local radius
        }
    }

    private List<String> determineDistributionChannels(String businessCategory) {
        String categoryLower = businessCategory.toLowerCase();

        if (categoryLower.contains("grocery") || categoryLower.contains("retail")) {
            return Arrays.asList(
                    "Direct retail store",
                    "Home delivery within 5 km",
                    "Weekly village market",
                    "WhatsApp-based orders"
            );
        } else if (categoryLower.contains("dairy") || categoryLower.contains("food")) {
            return Arrays.asList(
                    "Direct sales from shop",
                    "Morning door-to-door delivery",
                    "Supply to local shops",
                    "Bulk orders from nearby villages"
            );
        } else {
            return Arrays.asList(
                    "Physical store location",
                    "Local word-of-mouth marketing",
                    "Village community networks",
                    "Regional markets on market days"
            );
        }
    }

    private List<String> identifyUnderservedNiches(String businessCategory) {
        return Arrays.asList(
                "Quality-conscious middle-income families",
                "Young professionals returning to rural areas",
                "Small business owners needing reliable suppliers",
                "Elderly population requiring convenient access"
        );
    }

    private List<String> identifyLocalOpportunities(String businessCategory, LocationDTO location) {
        return Arrays.asList(
                "Growing rural economy with increasing purchasing power",
                "Limited competition in " + location.getVillage() + " area",
                "Strong community relationships enable trust-based business",
                "Government schemes supporting rural entrepreneurship",
                "Increasing digital literacy enabling online payments"
        );
    }

    private List<String> identifyLocalThreats(String businessCategory) {
        return Arrays.asList(
                "Seasonal income variations affecting purchasing patterns",
                "Competition from nearby towns (10-15 km away)",
                "Price sensitivity among rural consumers",
                "Potential entry of large organized retail chains"
        );
    }

    private String determinePricingGuidance(String businessCategory) {
        return "Price 5-10% below nearby town rates while maintaining quality. " +
               "Focus on value-for-money positioning rather than premium pricing. " +
               "Consider flexible payment options for regular customers.";
    }

    private BigDecimal estimateAverageIncome(LocationDTO location) {
        // Mock average monthly household income for rural areas
        // Varies by state and district (simplified here)
        return new BigDecimal("15000"); // ₹15,000 per month average
    }

    private String assessDemandLevel(String businessCategory) {
        String categoryLower = businessCategory.toLowerCase();

        if (categoryLower.contains("grocery") || categoryLower.contains("food") ||
            categoryLower.contains("daily needs")) {
            return "HIGH";
        } else if (categoryLower.contains("hardware") || categoryLower.contains("clothing")) {
            return "MEDIUM";
        } else {
            return "MEDIUM";
        }
    }

    private String assessCompetitionLevel(String businessCategory) {
        String categoryLower = businessCategory.toLowerCase();

        if (categoryLower.contains("grocery") || categoryLower.contains("general store")) {
            return "MEDIUM"; // Common business, moderate competition
        } else if (categoryLower.contains("electronics") || categoryLower.contains("specialized")) {
            return "LOW"; // Specialized businesses have less competition
        } else {
            return "MEDIUM";
        }
    }
}
