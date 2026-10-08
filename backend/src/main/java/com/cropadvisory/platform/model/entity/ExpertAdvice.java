package com.cropadvisory.platform.model.entity;

import com.cropadvisory.platform.model.enums.AdviceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a consultation between a farmer and an agricultural expert.
 *
 * <p>Tracks the farmer's question, the expert's response, and the
 * consultation status.</p>
 */
@Entity
@Table(name = "expert_advice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private FarmerProfile farmerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private ExpertProfile expertProfile;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(length = 2000)
    private String response;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AdviceStatus status = AdviceStatus.OPEN;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime askedAt;

    private LocalDateTime answeredAt;
}
