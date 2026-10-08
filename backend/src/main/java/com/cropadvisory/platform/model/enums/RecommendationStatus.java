package com.cropadvisory.platform.model.enums;

/**
 * Status lifecycle for crop recommendations.
 *
 * <p>Tracks the state of a recommendation from generation through farmer review.</p>
 */
public enum RecommendationStatus {
    GENERATED,
    VIEWED,
    ACCEPTED
}
