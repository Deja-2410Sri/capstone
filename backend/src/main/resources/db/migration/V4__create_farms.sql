CREATE TABLE farms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    farm_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    soil_type VARCHAR(100),
    area DOUBLE NOT NULL,
    irrigation_type VARCHAR(100),
    water_availability VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_farm_farmer FOREIGN KEY (farmer_id) REFERENCES farmer_profiles(id) ON DELETE CASCADE
);

CREATE INDEX idx_farms_farmer_id ON farms(farmer_id);
