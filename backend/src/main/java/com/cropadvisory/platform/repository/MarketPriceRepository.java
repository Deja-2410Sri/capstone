package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for MarketPrice entity database operations.
 */
@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

    List<MarketPrice> findByCropIdOrderByRecordedDateDesc(Long cropId);

    List<MarketPrice> findAllByOrderByRecordedDateDesc();
}
