CREATE TABLE crops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crop_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    season VARCHAR(50),
    soil_type VARCHAR(100),
    water_requirement VARCHAR(50),
    temperature_range VARCHAR(100),
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_crops_crop_name ON crops(crop_name);
CREATE INDEX idx_crops_season ON crops(season);
CREATE INDEX idx_crops_soil_type ON crops(soil_type);
CREATE INDEX idx_crops_category ON crops(category);
