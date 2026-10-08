package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.ExpertAdvice;
import com.cropadvisory.platform.model.enums.AdviceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ExpertAdvice entity database operations.
 */
@Repository
public interface ExpertAdviceRepository extends JpaRepository<ExpertAdvice, Long> {

    List<ExpertAdvice> findByFarmerProfileId(Long farmerProfileId);

    Page<ExpertAdvice> findByFarmerProfileId(Long farmerProfileId, Pageable pageable);

    List<ExpertAdvice> findByExpertProfileId(Long expertProfileId);

    Page<ExpertAdvice> findByExpertProfileId(Long expertProfileId, Pageable pageable);

    List<ExpertAdvice> findByStatus(AdviceStatus status);

    long countByStatus(AdviceStatus status);

    long countByFarmerProfileId(Long farmerProfileId);
}
