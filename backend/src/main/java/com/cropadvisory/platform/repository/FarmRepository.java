package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.Farm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Farm entity database operations.
 */
@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

    List<Farm> findByFarmerProfileId(Long farmerProfileId);

    Page<Farm> findByFarmerProfileId(Long farmerProfileId, Pageable pageable);

    long countByFarmerProfileId(Long farmerProfileId);
}
