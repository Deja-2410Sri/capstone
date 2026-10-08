CREATE TABLE crop_recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    crop_id BIGINT NOT NULL,
    reason TEXT,
    suitability_score DOUBLE,
    status VARCHAR(20) DEFAULT 'GENERATED',
    recommendation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recommendation_farmer FOREIGN KEY (farmer_id) REFERENCES farmer_profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendation_crop FOREIGN KEY (crop_id) REFERENCES crops(id) ON DELETE CASCADE
);

CREATE INDEX idx_recommendations_farmer_id ON crop_recommendations(farmer_id);
CREATE INDEX idx_recommendations_crop_id ON crop_recommendations(crop_id);
