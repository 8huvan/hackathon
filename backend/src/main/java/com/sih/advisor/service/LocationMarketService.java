package com.sih.advisor.service;

import com.sih.advisor.dto.LocationDTO;
import com.sih.advisor.dto.MarketAnalysisDTO;

/**
 * Service interface for retrieving hyper-local market data.
 * This abstraction allows for different implementations (mock, real API, database).
 *
 * The hyper-local approach focuses on a 5-10 km market radius from the business location.
 */
public interface LocationMarketService {

    /**
     * Analyzes the local market for a specific location and business category.
     *
     * @param location The location details (village, block, district, state)
     * @param businessCategory The business category to analyze
     * @return Market analysis data for the location
     */
    MarketAnalysisDTO analyzeLocalMarket(LocationDTO location, String businessCategory);

    /**
     * Estimates the consumer base within the market radius.
     *
     * @param location The location details
     * @return Estimated number of potential consumers
     */
    Integer estimateConsumerBase(LocationDTO location);

    /**
     * Determines the appropriate market reach radius in kilometers.
     *
     * @param businessCategory The business category
     * @return Market reach in kilometers (typically 5-10 km)
     */
    Integer determineMarketRadius(String businessCategory);
}
