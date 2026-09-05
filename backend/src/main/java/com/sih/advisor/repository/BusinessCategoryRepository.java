package com.sih.advisor.repository;

import com.sih.advisor.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for BusinessCategory entity operations.
 */
@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {

    /**
     * Find a business category by its name.
     *
     * @param category The category name
     * @return Optional containing the business category if found
     */
    Optional<BusinessCategory> findByCategory(String category);

    /**
     * Find all active business categories.
     *
     * @return List of active business categories
     */
    List<BusinessCategory> findByIsActiveTrue();

    /**
     * Find business categories suitable for a specific location type.
     *
     * @param suitableFor Location type (rural/urban/both)
     * @return List of suitable business categories
     */
    List<BusinessCategory> findBySuitableForAndIsActiveTrue(String suitableFor);
}
