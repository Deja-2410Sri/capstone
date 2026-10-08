package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.farmer.FarmerProfileResponse;
import com.cropadvisory.platform.dto.farmer.FarmerProfileUpdateRequest;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.FarmerProfile;
import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.repository.FarmerProfileRepository;
import com.cropadvisory.platform.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for farmer profile management.
 */
@Service
public class FarmerService {

    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;

    public FarmerService(FarmerProfileRepository farmerProfileRepository,
                         UserRepository userRepository) {
        this.farmerProfileRepository = farmerProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the farmer profile for the given user email.
     *
     * @param email the user's email
     * @return farmer profile response
     */
    public FarmerProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));

        return mapToResponse(profile, user);
    }

    /**
     * Updates the farmer profile for the given user email.
     *
     * @param email   the user's email
     * @param request the update request
     * @return updated farmer profile response
     */
    @Transactional
    public FarmerProfileResponse updateProfile(String email, FarmerProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));

        if (request.getLocation() != null) profile.setLocation(request.getLocation());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getDistrict() != null) profile.setDistrict(request.getDistrict());
        if (request.getState() != null) profile.setState(request.getState());
        if (request.getPostalCode() != null) profile.setPostalCode(request.getPostalCode());
        if (request.getSoilType() != null) profile.setSoilType(request.getSoilType());
        if (request.getFarmSize() != null) profile.setFarmSize(request.getFarmSize());

        profile = farmerProfileRepository.save(profile);
        return mapToResponse(profile, user);
    }

    private FarmerProfileResponse mapToResponse(FarmerProfile profile, User user) {
        return FarmerProfileResponse.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .location(profile.getLocation())
                .address(profile.getAddress())
                .district(profile.getDistrict())
                .state(profile.getState())
                .postalCode(profile.getPostalCode())
                .soilType(profile.getSoilType())
                .farmSize(profile.getFarmSize())
                .build();
    }
}
