package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.FarmingGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FarmingGuideline entity database operations.
 */
@Repository
public interface FarmingGuidelineRepository extends JpaRepository<FarmingGuideline, Long> {

    List<FarmingGuideline> findByCropId(Long cropId);
}
