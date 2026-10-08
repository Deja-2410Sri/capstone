CREATE TABLE farming_guidelines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crop_id BIGINT NOT NULL,
    sowing_method TEXT,
    irrigation TEXT,
    fertilizer TEXT,
    pest_management TEXT,
    harvesting TEXT,
    general_tips TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_guideline_crop FOREIGN KEY (crop_id) REFERENCES crops(id) ON DELETE CASCADE
);

CREATE INDEX idx_guidelines_crop_id ON farming_guidelines(crop_id);
