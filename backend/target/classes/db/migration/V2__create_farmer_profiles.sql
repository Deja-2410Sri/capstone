CREATE TABLE farmer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    location VARCHAR(255),
    address VARCHAR(500),
    district VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    soil_type VARCHAR(100),
    farm_size DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_farmer_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
