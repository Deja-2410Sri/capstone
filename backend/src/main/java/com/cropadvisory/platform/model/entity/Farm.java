package com.cropadvisory.platform.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a farm owned by a farmer.
 *
 * <p>Stores location, soil type, area, and irrigation details used
 * by the crop recommendation engine.</p>
 */
@Entity
@Table(name = "farms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private FarmerProfile farmerProfile;

    @Column(nullable = false)
    private String farmName;

    private String location;

    private String soilType;

    @NotNull
    @Positive
    private Double area;

    private String irrigationType;

    private String waterAvailability;

    @OneToOne(mappedBy = "farm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WeatherInformation weatherInformation;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
