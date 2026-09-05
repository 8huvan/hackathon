package com.sih.advisor.service;

import com.sih.advisor.dto.BusinessCategoryDTO;
import com.sih.advisor.entity.BusinessCategory;
import com.sih.advisor.exception.ResourceNotFoundException;
import com.sih.advisor.repository.BusinessCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BusinessCategoryService.
 */
@ExtendWith(MockitoExtension.class)
class BusinessCategoryServiceTest {

    @Mock
    private BusinessCategoryRepository businessCategoryRepository;

    @InjectMocks
    private BusinessCategoryService businessCategoryService;

    private BusinessCategory groceryCategory;
    private BusinessCategory hardwareCategory;

    @BeforeEach
    void setUp() {
        groceryCategory = BusinessCategory.builder()
                .id(1L)
                .category("Grocery Store")
                .businessType("Retail")
                .description("General grocery and daily needs store")
                .minInvestment(new BigDecimal("50000"))
                .maxInvestment(new BigDecimal("500000"))
                .requiredResources("Shop space, inventory, display shelves")
                .targetCustomers("Local residents, families")
                .distributionConsiderations("Daily delivery, home service")
                .isActive(true)
                .suitableFor("both")
                .typicalMarketRadiusKm(5)
                .build();

        hardwareCategory = BusinessCategory.builder()
                .id(2L)
                .category("Hardware Store")
                .businessType("Retail")
                .description("Construction and hardware materials")
                .minInvestment(new BigDecimal("100000"))
                .maxInvestment(new BigDecimal("1000000"))
                .requiredResources("Warehouse, inventory, tools")
                .targetCustomers("Builders, home owners")
                .distributionConsiderations("Bulk delivery, on-site service")
                .isActive(true)
                .suitableFor("rural")
                .typicalMarketRadiusKm(10)
                .build();
    }

    @Test
    void testGetAllActiveCategories() {
        // Given
        when(businessCategoryRepository.findByIsActiveTrue())
                .thenReturn(Arrays.asList(groceryCategory, hardwareCategory));

        // When
        List<BusinessCategoryDTO> categories = businessCategoryService.getAllActiveCategories();

        // Then
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Grocery Store", categories.get(0).getCategory());
        assertEquals("Hardware Store", categories.get(1).getCategory());

        verify(businessCategoryRepository).findByIsActiveTrue();
    }

    @Test
    void testGetCategoryByName_Success() {
        // Given
        when(businessCategoryRepository.findByCategory("Grocery Store"))
                .thenReturn(Optional.of(groceryCategory));

        // When
        BusinessCategoryDTO category = businessCategoryService.getCategoryByName("Grocery Store");

        // Then
        assertNotNull(category);
        assertEquals("Grocery Store", category.getCategory());
        assertEquals("Retail", category.getBusinessType());
        assertEquals(new BigDecimal("50000"), category.getMinInvestment());
        assertEquals(new BigDecimal("500000"), category.getMaxInvestment());
        assertEquals(5, category.getTypicalMarketRadiusKm());

        verify(businessCategoryRepository).findByCategory("Grocery Store");
    }

    @Test
    void testGetCategoryByName_NotFound() {
        // Given
        when(businessCategoryRepository.findByCategory("NonExistent"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                businessCategoryService.getCategoryByName("NonExistent")
        );

        verify(businessCategoryRepository).findByCategory("NonExistent");
    }

    @Test
    void testGetCategoriesBySuitability_Rural() {
        // Given
        when(businessCategoryRepository.findBySuitableForAndIsActiveTrue("rural"))
                .thenReturn(Arrays.asList(hardwareCategory));

        // When
        List<BusinessCategoryDTO> categories = businessCategoryService.getCategoriesBySuitability("rural");

        // Then
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Hardware Store", categories.get(0).getCategory());
        assertEquals("rural", categories.get(0).getSuitableFor());

        verify(businessCategoryRepository).findBySuitableForAndIsActiveTrue("rural");
    }

    @Test
    void testGetCategoriesBySuitability_Both() {
        // Given
        when(businessCategoryRepository.findBySuitableForAndIsActiveTrue("both"))
                .thenReturn(Arrays.asList(groceryCategory));

        // When
        List<BusinessCategoryDTO> categories = businessCategoryService.getCategoriesBySuitability("both");

        // Then
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Grocery Store", categories.get(0).getCategory());

        verify(businessCategoryRepository).findBySuitableForAndIsActiveTrue("both");
    }

    @Test
    void testGetAllActiveCategories_Empty() {
        // Given
        when(businessCategoryRepository.findByIsActiveTrue())
                .thenReturn(Arrays.asList());

        // When
        List<BusinessCategoryDTO> categories = businessCategoryService.getAllActiveCategories();

        // Then
        assertNotNull(categories);
        assertTrue(categories.isEmpty());

        verify(businessCategoryRepository).findByIsActiveTrue();
    }
}
