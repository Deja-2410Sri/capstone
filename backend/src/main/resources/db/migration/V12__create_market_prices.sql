CREATE TABLE market_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crop_id BIGINT NOT NULL,
    market_name VARCHAR(255),
    location VARCHAR(255),
    price DECIMAL(12, 2) NOT NULL,
    unit VARCHAR(50),
    recorded_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_market_crop FOREIGN KEY (crop_id) REFERENCES crops(id) ON DELETE CASCADE
);

CREATE INDEX idx_market_crop_id ON market_prices(crop_id);
