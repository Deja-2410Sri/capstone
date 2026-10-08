package com.cropadvisory.platform.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Farming guidance information for a specific crop.
 *
 * <p>Contains sowing, irrigation, fertilizer, pest management, and harvesting
 * guidelines created by experts or administrators.</p>
 */
@Entity
@Table(name = "farming_guidelines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmingGuideline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @Column(length = 2000)
    private String sowingMethod;

    @Column(length = 2000)
    private String irrigation;

    @Column(length = 2000)
    private String fertilizer;

    @Column(length = 2000)
    private String pestManagement;

    @Column(length = 2000)
    private String harvesting;

    @Column(length = 2000)
    private String generalTips;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
