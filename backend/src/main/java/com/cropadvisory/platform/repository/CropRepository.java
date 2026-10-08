package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.Crop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Crop entity database operations.
 *
 * <p>Supports dynamic search filtering via JpaSpecificationExecutor.</p>
 */
@Repository
public interface CropRepository extends JpaRepository<Crop, Long>, JpaSpecificationExecutor<Crop> {

    List<Crop> findBySeason(String season);

    List<Crop> findBySoilType(String soilType);

    List<Crop> findByCategory(String category);

    List<Crop> findByActiveTrue();

    Page<Crop> findByActiveTrue(Pageable pageable);

    long countByActiveTrue();
}
