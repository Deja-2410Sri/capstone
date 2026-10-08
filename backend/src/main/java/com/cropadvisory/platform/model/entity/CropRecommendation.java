package com.cropadvisory.platform.model.entity;

import com.cropadvisory.platform.model.enums.RecommendationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * A crop recommendation generated for a specific farmer based on farm conditions.
 *
 * <p>Contains the recommended crop, a suitability score, and the reasoning
 * behind the recommendation.</p>
 */
@Entity
@Table(name = "crop_recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private FarmerProfile farmerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @Column(length = 1000)
    private String reason;

    private Double suitabilityScore;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RecommendationStatus status = RecommendationStatus.GENERATED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime recommendationDate;
}
