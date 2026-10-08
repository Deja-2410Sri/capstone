package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.WeatherInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for WeatherInformation entity database operations.
 */
@Repository
public interface WeatherInformationRepository extends JpaRepository<WeatherInformation, Long> {

    Optional<WeatherInformation> findByFarmId(Long farmId);

    Optional<WeatherInformation> findByLocation(String location);
}
