package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.advice.AdviceAnswerRequest;
import com.cropadvisory.platform.dto.advice.AdviceRequest;
import com.cropadvisory.platform.dto.advice.AdviceResponse;
import com.cropadvisory.platform.exception.ForbiddenException;
import com.cropadvisory.platform.exception.ResourceNotFoundException;
import com.cropadvisory.platform.model.entity.*;
import com.cropadvisory.platform.model.enums.AdviceStatus;
import com.cropadvisory.platform.model.enums.NotificationType;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for expert consultation management.
 */
@Service
public class ExpertAdviceService {

    private static final Logger log = LoggerFactory.getLogger(ExpertAdviceService.class);

    private final ExpertAdviceRepository adviceRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ExpertAdviceService(ExpertAdviceRepository adviceRepository,
                               FarmerProfileRepository farmerProfileRepository,
                               ExpertProfileRepository expertProfileRepository,
                               UserRepository userRepository,
                               NotificationService notificationService) {
        this.adviceRepository = adviceRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AdviceResponse askExpert(String email, AdviceRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));

        ExpertAdvice advice = ExpertAdvice.builder()
                .farmerProfile(profile)
                .question(request.getQuestion())
                .status(AdviceStatus.OPEN)
                .build();
        advice = adviceRepository.save(advice);

        notificationService.createNotification(user, "New Expert Question",
                "Your question has been submitted: " + request.getQuestion().substring(0, Math.min(50, request.getQuestion().length())),
                NotificationType.NEW_EXPERT_QUESTION);

        log.info("Expert question created by farmer: {}", email);
        return mapToResponse(advice);
    }

    public Page<AdviceResponse> getAdviceForFarmer(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FarmerProfile", "userId", user.getId()));
        return adviceRepository.findByFarmerProfileId(profile.getId(), pageable).map(this::mapToResponse);
    }

    public Page<AdviceResponse> getAdviceForExpert(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        ExpertProfile profile = expertProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpertProfile", "userId", user.getId()));
        return adviceRepository.findByExpertProfileId(profile.getId(), pageable).map(this::mapToResponse);
    }

    @Transactional
    public AdviceResponse answerQuestion(Long id, String expertEmail, AdviceAnswerRequest request) {
        ExpertAdvice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpertAdvice", "id", id));

        User expertUser = userRepository.findByEmail(expertEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", expertEmail));
        ExpertProfile expertProfile = expertProfileRepository.findByUserId(expertUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpertProfile", "userId", expertUser.getId()));

        advice.setExpertProfile(expertProfile);
        advice.setResponse(request.getResponse());
        advice.setStatus(AdviceStatus.ANSWERED);
        advice.setAnsweredAt(LocalDateTime.now());
        advice = adviceRepository.save(advice);

        notificationService.createNotification(advice.getFarmerProfile().getUser(),
                "Expert Answered Your Question",
                "Your question has been answered by an expert.",
                NotificationType.EXPERT_ANSWERED);

        log.info("Expert advice provided by: {} for advice id: {}", expertEmail, id);
        return mapToResponse(advice);
    }

    public long countOpenQuestions() {
        return adviceRepository.countByStatus(AdviceStatus.OPEN);
    }

    private AdviceResponse mapToResponse(ExpertAdvice a) {
        return AdviceResponse.builder()
                .id(a.getId())
                .farmerId(a.getFarmerProfile().getId())
                .farmerName(a.getFarmerProfile().getUser().getFirstName() + " " + a.getFarmerProfile().getUser().getLastName())
                .expertId(a.getExpertProfile() != null ? a.getExpertProfile().getId() : null)
                .expertName(a.getExpertProfile() != null ? a.getExpertProfile().getUser().getFirstName() + " " + a.getExpertProfile().getUser().getLastName() : null)
                .question(a.getQuestion())
                .response(a.getResponse())
                .status(a.getStatus().name())
                .askedAt(a.getAskedAt())
                .answeredAt(a.getAnsweredAt())
                .build();
    }
}
