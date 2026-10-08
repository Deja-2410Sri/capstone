package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.disease.DiseaseRequest;
import com.cropadvisory.platform.dto.disease.DiseaseResponse;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.Crop;
import com.cropadvisory.platform.model.entity.DiseasePest;
import com.cropadvisory.platform.repository.CropRepository;
import com.cropadvisory.platform.repository.DiseasePestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for disease/pest information management.
 */
@Service
public class DiseaseService {

    private final DiseasePestRepository diseasePestRepository;
    private final CropRepository cropRepository;

    public DiseaseService(DiseasePestRepository diseasePestRepository, CropRepository cropRepository) {
        this.diseasePestRepository = diseasePestRepository;
        this.cropRepository = cropRepository;
    }

    public List<DiseaseResponse> getAllDiseases() {
        return diseasePestRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<DiseaseResponse> getDiseasesByCropId(Long cropId) {
        return diseasePestRepository.findByCropId(cropId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<DiseaseResponse> searchDiseases(Long cropId, String name) {
        return diseasePestRepository.findByCropIdAndDiseaseNameContainingIgnoreCase(cropId, name)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public DiseaseResponse createDisease(DiseaseRequest request) {
        Crop crop = cropRepository.findById(request.getCropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "id", request.getCropId()));
        DiseasePest disease = DiseasePest.builder()
                .crop(crop).diseaseName(request.getDiseaseName())
                .symptoms(request.getSymptoms()).causes(request.getCauses())
                .prevention(request.getPrevention()).treatment(request.getTreatment()).build();
        disease = diseasePestRepository.save(disease);
        return mapToResponse(disease);
    }

    @Transactional
    public DiseaseResponse updateDisease(Long id, DiseaseRequest request) {
        DiseasePest disease = diseasePestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease", "id", id));
        if (request.getDiseaseName() != null) disease.setDiseaseName(request.getDiseaseName());
        if (request.getSymptoms() != null) disease.setSymptoms(request.getSymptoms());
        if (request.getCauses() != null) disease.setCauses(request.getCauses());
        if (request.getPrevention() != null) disease.setPrevention(request.getPrevention());
        if (request.getTreatment() != null) disease.setTreatment(request.getTreatment());
        disease = diseasePestRepository.save(disease);
        return mapToResponse(disease);
    }

    @Transactional
    public void deleteDisease(Long id) {
        diseasePestRepository.deleteById(id);
    }

    private DiseaseResponse mapToResponse(DiseasePest d) {
        return DiseaseResponse.builder()
                .id(d.getId()).cropId(d.getCrop().getId()).cropName(d.getCrop().getCropName())
                .diseaseName(d.getDiseaseName()).symptoms(d.getSymptoms())
                .causes(d.getCauses()).prevention(d.getPrevention())
                .treatment(d.getTreatment()).createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt()).build();
    }
}
