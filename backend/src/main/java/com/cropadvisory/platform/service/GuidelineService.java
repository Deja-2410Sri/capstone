package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.guideline.GuidelineRequest;
import com.cropadvisory.platform.dto.guideline.GuidelineResponse;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.Crop;
import com.cropadvisory.platform.model.entity.FarmingGuideline;
import com.cropadvisory.platform.repository.CropRepository;
import com.cropadvisory.platform.repository.FarmingGuidelineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for farming guideline management.
 */
@Service
public class GuidelineService {

    private final FarmingGuidelineRepository guidelineRepository;
    private final CropRepository cropRepository;

    public GuidelineService(FarmingGuidelineRepository guidelineRepository, CropRepository cropRepository) {
        this.guidelineRepository = guidelineRepository;
        this.cropRepository = cropRepository;
    }

    public List<GuidelineResponse> getAllGuidelines() {
        return guidelineRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<GuidelineResponse> getGuidelinesByCropId(Long cropId) {
        return guidelineRepository.findByCropId(cropId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public GuidelineResponse createGuideline(GuidelineRequest request) {
        Crop crop = cropRepository.findById(request.getCropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "id", request.getCropId()));
        FarmingGuideline guideline = FarmingGuideline.builder()
                .crop(crop).sowingMethod(request.getSowingMethod())
                .irrigation(request.getIrrigation()).fertilizer(request.getFertilizer())
                .pestManagement(request.getPestManagement()).harvesting(request.getHarvesting())
                .generalTips(request.getGeneralTips()).build();
        guideline = guidelineRepository.save(guideline);
        return mapToResponse(guideline);
    }

    @Transactional
    public GuidelineResponse updateGuideline(Long id, GuidelineRequest request) {
        FarmingGuideline guideline = guidelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guideline", "id", id));
        if (request.getSowingMethod() != null) guideline.setSowingMethod(request.getSowingMethod());
        if (request.getIrrigation() != null) guideline.setIrrigation(request.getIrrigation());
        if (request.getFertilizer() != null) guideline.setFertilizer(request.getFertilizer());
        if (request.getPestManagement() != null) guideline.setPestManagement(request.getPestManagement());
        if (request.getHarvesting() != null) guideline.setHarvesting(request.getHarvesting());
        if (request.getGeneralTips() != null) guideline.setGeneralTips(request.getGeneralTips());
        guideline = guidelineRepository.save(guideline);
        return mapToResponse(guideline);
    }

    @Transactional
    public void deleteGuideline(Long id) {
        guidelineRepository.deleteById(id);
    }

    private GuidelineResponse mapToResponse(FarmingGuideline g) {
        return GuidelineResponse.builder()
                .id(g.getId()).cropId(g.getCrop().getId()).cropName(g.getCrop().getCropName())
                .sowingMethod(g.getSowingMethod()).irrigation(g.getIrrigation())
                .fertilizer(g.getFertilizer()).pestManagement(g.getPestManagement())
                .harvesting(g.getHarvesting()).generalTips(g.getGeneralTips())
                .createdAt(g.getCreatedAt()).updatedAt(g.getUpdatedAt()).build();
    }
}
