package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.recommendation.RecommendationResponse;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.*;
import com.cropadvisory.platform.model.enums.RecommendationStatus;
import com.cropadvisory.platform.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for crop recommendation engine.
 *
 * <p>Implements a rule-based recommendation algorithm that matches farm
 * conditions with crop requirements to generate suitability scores.</p>
 */
@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final CropRecommendationRepository recommendationRepository;
    private final CropRepository cropRepository;
    private final FarmRepository farmRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;

    public RecommendationService(CropRecommendationRepository recommendationRepository,
                                 CropRepository cropRepository,
                                 FarmRepository farmRepository,
                                 FarmerProfileRepository farmerProfileRepository,
                                 UserRepository userRepository) {
        this.recommendationRepository = recommendationRepository;
        this.cropRepository = cropRepository;
        this.farmRepository = farmRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<RecommendationResponse> generateRecommendations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));
        List<Farm> farms = farmRepository.findByFarmerProfileId(profile.getId());
        if (farms.isEmpty()) return new ArrayList<>();

        List<Crop> activeCrops = cropRepository.findByActiveTrue();
        List<RecommendationResponse> recommendations = new ArrayList<>();

        for (Farm farm : farms) {
            for (Crop crop : activeCrops) {
                double score = calculateSuitability(crop, farm);
                if (score > 0 && !recommendationRepository.existsByFarmerProfileIdAndCropId(profile.getId(), crop.getId())) {
                    String reason = generateReason(crop, farm, score);
                    CropRecommendation rec = CropRecommendation.builder()
                            .farmerProfile(profile).crop(crop)
                            .suitabilityScore(score).reason(reason)
                            .status(RecommendationStatus.GENERATED).build();
                    rec = recommendationRepository.save(rec);
                    recommendations.add(mapToResponse(rec));
                }
            }
        }
        log.info("Generated {} recommendations for farmer: {}", recommendations.size(), email);
        return recommendations;
    }

    public double calculateSuitability(Crop crop, Farm farm) {
        double score = 0;
        if (crop.getSoilType() != null && farm.getSoilType() != null) {
            if (crop.getSoilType().equalsIgnoreCase(farm.getSoilType())) score += 30;
            else if (crop.getSoilType().toLowerCase().contains("any")) score += 15;
        }
        if (crop.getWaterRequirement() != null && farm.getWaterAvailability() != null) {
            String wr = crop.getWaterRequirement().toLowerCase();
            String wa = farm.getWaterAvailability().toLowerCase();
            if (wr.contains("high") && wa.contains("high")) score += 25;
            else if (wr.contains("medium") && (wa.contains("medium") || wa.contains("high"))) score += 25;
            else if (wr.contains("low") && wa.contains("low")) score += 25;
            else score += 5;
        }
        if (crop.getSeason() != null) score += 20;
        if (farm.getArea() != null && farm.getArea() > 0) score += 15;
        if (crop.getActive()) score += 10;
        return score;
    }
     @Transactional(readOnly = true)
    public Page<RecommendationResponse> getRecommendations(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));
        return recommendationRepository.findByFarmerProfileId(profile.getId(), pageable).map(this::mapToResponse);
    }
     @Transactional(readOnly = true)
    public RecommendationResponse getRecommendationById(Long id) {
        CropRecommendation rec = recommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation", "id", id));
        return mapToResponse(rec);
    }

    private String generateReason(Crop crop, Farm farm, double score) {
        StringBuilder sb = new StringBuilder();
        if (crop.getSoilType() != null && crop.getSoilType().equalsIgnoreCase(farm.getSoilType())) {
            sb.append("Soil type matches. ");
        }
        if (crop.getSeason() != null) sb.append("Suitable for ").append(crop.getSeason()).append(" season. ");
        if (score >= 70) sb.append("Highly suitable crop for your farm conditions.");
        else if (score >= 40) sb.append("Moderately suitable for your farm conditions.");
        else sb.append("Marginally suitable - consider alternatives.");
        return sb.toString();
    }

    private RecommendationResponse mapToResponse(CropRecommendation rec) {
        return RecommendationResponse.builder()
                .id(rec.getId())
                .farmerId(rec.getFarmerProfile().getId())
                .cropId(rec.getCrop().getId())
                .cropName(rec.getCrop().getCropName())
                .season(rec.getCrop().getSeason())
                .soilType(rec.getCrop().getSoilType())
                .reason(rec.getReason())
                .suitabilityScore(rec.getSuitabilityScore())
                .status(rec.getStatus().name())
                .recommendationDate(rec.getRecommendationDate())
                .build();
    }
}
