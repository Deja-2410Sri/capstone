package com.cropadvisory.platform.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Disease and pest information associated with a specific crop.
 *
 * <p>Stores symptoms, causes, prevention, and treatment information
 * for farmer education and advisory purposes.</p>
 */
@Entity
@Table(name = "disease_pests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseasePest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @Column(nullable = false)
    private String diseaseName;

    @Column(length = 2000)
    private String symptoms;

    @Column(length = 2000)
    private String causes;

    @Column(length = 2000)
    private String prevention;

    @Column(length = 2000)
    private String treatment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
