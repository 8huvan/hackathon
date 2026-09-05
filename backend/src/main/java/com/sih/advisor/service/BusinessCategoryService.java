package com.sih.advisor.service;

import com.sih.advisor.dto.BusinessCategoryDTO;
import com.sih.advisor.entity.BusinessCategory;
import com.sih.advisor.exception.ResourceNotFoundException;
import com.sih.advisor.repository.BusinessCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing business categories.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessCategoryService {

    private final BusinessCategoryRepository businessCategoryRepository;

    /**
     * Retrieves all active business categories.
     *
     * @return List of active business category DTOs
     */
    @Transactional(readOnly = true)
    public List<BusinessCategoryDTO> getAllActiveCategories() {
        log.debug("Fetching all active business categories");
        return businessCategoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a business category by its name.
     *
     * @param category The category name
     * @return Business category DTO
     * @throws ResourceNotFoundException if category not found
     */
    @Transactional(readOnly = true)
    public BusinessCategoryDTO getCategoryByName(String category) {
        log.debug("Fetching business category: {}", category);
        BusinessCategory businessCategory = businessCategoryRepository.findByCategory(category)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business category not found: " + category));
        return convertToDTO(businessCategory);
    }

    /**
     * Retrieves business categories suitable for a specific location type.
     *
     * @param locationType Location type (rural/urban/both)
     * @return List of suitable business category DTOs
     */
    @Transactional(readOnly = true)
    public List<BusinessCategoryDTO> getCategoriesBySuitability(String locationType) {
        log.debug("Fetching business categories suitable for: {}", locationType);
        return businessCategoryRepository.findBySuitableForAndIsActiveTrue(locationType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a BusinessCategory entity to DTO.
     *
     * @param entity The entity to convert
     * @return Business category DTO
     */
    private BusinessCategoryDTO convertToDTO(BusinessCategory entity) {
        return BusinessCategoryDTO.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .businessType(entity.getBusinessType())
                .description(entity.getDescription())
                .minInvestment(entity.getMinInvestment())
                .maxInvestment(entity.getMaxInvestment())
                .requiredResources(entity.getRequiredResources())
                .targetCustomers(entity.getTargetCustomers())
                .distributionConsiderations(entity.getDistributionConsiderations())
                .suitableFor(entity.getSuitableFor())
                .typicalMarketRadiusKm(entity.getTypicalMarketRadiusKm())
                .build();
    }
}
