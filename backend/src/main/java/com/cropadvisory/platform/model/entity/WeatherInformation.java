package com.cropadvisory.platform.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Weather information recorded for a specific farm location.
 *
 * <p>Stores temperature, humidity, rainfall, and weather condition data
 * used by the recommendation engine and farmer dashboard.</p>
 */
@Entity
@Table(name = "weather_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private Double temperature;

    private Double humidity;

    private Double rainfall;

    private String weatherCondition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime recordedAt;
}
