package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.crop.CropRequest;
import com.cropadvisory.platform.dto.crop.CropResponse;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.Crop;
import com.cropadvisory.platform.repository.CropRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for crop management and search operations.
 *
 * <p>Supports dynamic filtering by crop name, category, season, and soil type
 * using JPA Specifications.</p>
 */
@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    /**
     * Retrieves crops with optional search filters.
     *
     * @param name     optional crop name filter
     * @param category optional category filter
     * @param season   optional season filter
     * @param soilType optional soil type filter
     * @param pageable pagination parameters
     * @return paginated crop responses
     */
    public Page<CropResponse> searchCrops(String name, String category, String season, String soilType, Pageable pageable) {
        Specification<Crop> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("cropName")), "%" + name.toLowerCase() + "%"));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }
        if (season != null && !season.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("season")), season.toLowerCase()));
        }
        if (soilType != null && !soilType.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("soilType")), soilType.toLowerCase()));
        }

        spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));

        return cropRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    /**
     * Retrieves all active crops.
     *
     * @param pageable pagination parameters
     * @return paginated crop responses
     */
    public Page<CropResponse> getAllCrops(Pageable pageable) {
        return cropRepository.findByActiveTrue(pageable).map(this::mapToResponse);
    }

    /**
     * Retrieves a crop by ID.
     *
     * @param id the crop ID
     * @return crop response
     */
    public CropResponse getCropById(Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "id", id));
        return mapToResponse(crop);
    }

    /**
     * Creates a new crop.
     *
     * @param request the crop creation request
     * @return created crop response
     */
    @Transactional
    public CropResponse createCrop(CropRequest request) {
        Crop crop = Crop.builder()
                .cropName(request.getCropName())
                .category(request.getCategory())
                .season(request.getSeason())
                .soilType(request.getSoilType())
                .waterRequirement(request.getWaterRequirement())
                .temperatureRange(request.getTemperatureRange())
                .description(request.getDescription())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        crop = cropRepository.save(crop);
        return mapToResponse(crop);
    }

    /**
     * Updates an existing crop.
     *
     * @param id      the crop ID
     * @param request the crop update request
     * @return updated crop response
     */
    @Transactional
    public CropResponse updateCrop(Long id, CropRequest request) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "id", id));

        if (request.getCropName() != null) crop.setCropName(request.getCropName());
        if (request.getCategory() != null) crop.setCategory(request.getCategory());
        if (request.getSeason() != null) crop.setSeason(request.getSeason());
        if (request.getSoilType() != null) crop.setSoilType(request.getSoilType());
        if (request.getWaterRequirement() != null) crop.setWaterRequirement(request.getWaterRequirement());
        if (request.getTemperatureRange() != null) crop.setTemperatureRange(request.getTemperatureRange());
        if (request.getDescription() != null) crop.setDescription(request.getDescription());
        if (request.getActive() != null) crop.setActive(request.getActive());

        crop = cropRepository.save(crop);
        return mapToResponse(crop);
    }

    /**
     * Deletes a crop (soft delete by setting active to false).
     *
     * @param id the crop ID
     */
    @Transactional
    public void deleteCrop(Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "id", id));
        crop.setActive(false);
        cropRepository.save(crop);
    }

    private CropResponse mapToResponse(Crop crop) {
        return CropResponse.builder()
                .id(crop.getId())
                .cropName(crop.getCropName())
                .category(crop.getCategory())
                .season(crop.getSeason())
                .soilType(crop.getSoilType())
                .waterRequirement(crop.getWaterRequirement())
                .temperatureRange(crop.getTemperatureRange())
                .description(crop.getDescription())
                .active(crop.getActive())
                .createdAt(crop.getCreatedAt())
                .updatedAt(crop.getUpdatedAt())
                .build();
    }
}
