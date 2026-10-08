package com.cropadvisory.platform.repository;

import com.cropadvisory.platform.model.entity.DiseasePest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for DiseasePest entity database operations.
 */
@Repository
public interface DiseasePestRepository extends JpaRepository<DiseasePest, Long> {

    List<DiseasePest> findByCropId(Long cropId);

    List<DiseasePest> findByCropIdAndDiseaseNameContainingIgnoreCase(Long cropId, String diseaseName);
}
