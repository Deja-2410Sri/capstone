package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.CropRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CropRecommendation entity database operations.
 */
@Repository
public interface CropRecommendationRepository extends JpaRepository<CropRecommendation, Long> {

    List<CropRecommendation> findByFarmerProfileId(Long farmerProfileId);

    Page<CropRecommendation> findByFarmerProfileId(Long farmerProfileId, Pageable pageable);

    long countByFarmerProfileId(Long farmerProfileId);

    boolean existsByFarmerProfileIdAndCropId(Long farmerProfileId, Long cropId);
}
