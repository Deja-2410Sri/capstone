package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.farm.FarmRequest;
import com.cropadvisory.platform.dto.farm.FarmResponse;
import com.cropadvisory.platform.exception.BadRequestException;
import com.cropadvisory.platform.exception.ForbiddenException;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.Farm;
import com.cropadvisory.platform.model.entity.FarmerProfile;
import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.repository.FarmRepository;
import com.cropadvisory.platform.repository.FarmerProfileRepository;
import com.cropadvisory.platform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for farm management operations.
 *
 * <p>Handles CRUD operations for farms with ownership verification
 * to ensure farmers can only access their own farms.</p>
 */
@Service
public class FarmService {

    private final FarmRepository farmRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;

    public FarmService(FarmRepository farmRepository,
                       FarmerProfileRepository farmerProfileRepository,
                       UserRepository userRepository) {
        this.farmRepository = farmRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all farms for the authenticated farmer.
     *
     * @param email    the farmer's email
     * @param pageable pagination parameters
     * @return paginated farm responses
     */
    public Page<FarmResponse> getFarmsByEmail(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));

        return farmRepository.findByFarmerProfileId(profile.getId(), pageable)
                .map(this::mapToResponse);
    }

    /**
     * Retrieves a specific farm by ID with ownership verification.
     *
     * @param id    the farm ID
     * @param email the requesting user's email
     * @return farm response
     */
    @Transactional(readOnly = true)
    public FarmResponse getFarmById(Long id, String email) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", id));

        verifyOwnership(farm, email);
        return mapToResponse(farm);
    }

    /**
     * Creates a new farm for the authenticated farmer.
     *
     * @param email   the farmer's email
     * @param request the farm creation request
     * @return created farm response
     */
    @Transactional
    public FarmResponse createFarm(String email, FarmRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));

        Farm farm = Farm.builder()
                .farmerProfile(profile)
                .farmName(request.getFarmName())
                .location(request.getLocation())
                .soilType(request.getSoilType())
                .area(request.getArea())
                .irrigationType(request.getIrrigationType())
                .waterAvailability(request.getWaterAvailability())
                .build();

        farm = farmRepository.save(farm);
        return mapToResponse(farm);
    }

    /**
     * Updates an existing farm with ownership verification.
     *
     * @param id      the farm ID
     * @param email   the farmer's email
     * @param request the farm update request
     * @return updated farm response
     */
    @Transactional
    public FarmResponse updateFarm(Long id, String email, FarmRequest request) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", id));

        verifyOwnership(farm, email);

        if (request.getFarmName() != null) farm.setFarmName(request.getFarmName());
        if (request.getLocation() != null) farm.setLocation(request.getLocation());
        if (request.getSoilType() != null) farm.setSoilType(request.getSoilType());
        if (request.getArea() != null) farm.setArea(request.getArea());
        if (request.getIrrigationType() != null) farm.setIrrigationType(request.getIrrigationType());
        if (request.getWaterAvailability() != null) farm.setWaterAvailability(request.getWaterAvailability());

        farm = farmRepository.save(farm);
        return mapToResponse(farm);
    }

    /**
     * Deletes a farm with ownership verification.
     *
     * @param id    the farm ID
     * @param email the farmer's email
     */
    @Transactional
    public void deleteFarm(Long id, String email) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", id));

        verifyOwnership(farm, email);
        farmRepository.delete(farm);
    }

    private void verifyOwnership(Farm farm, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.getRole() == Role.ROLE_ADMIN) {
            return;
        }

        if (!farm.getFarmerProfile().getUser().getEmail().equals(email)) {
            throw new ForbiddenException("You do not have access to this farm");
        }
    }

    private FarmResponse mapToResponse(Farm farm) {
        return FarmResponse.builder()
                .id(farm.getId())
                .farmName(farm.getFarmName())
                .location(farm.getLocation())
                .soilType(farm.getSoilType())
                .area(farm.getArea())
                .irrigationType(farm.getIrrigationType())
                .waterAvailability(farm.getWaterAvailability())
                .farmerId(farm.getFarmerProfile().getId())
                .createdAt(farm.getCreatedAt())
                .updatedAt(farm.getUpdatedAt())
                .build();
    }
}
