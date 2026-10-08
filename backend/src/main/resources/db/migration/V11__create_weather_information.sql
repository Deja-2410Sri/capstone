CREATE TABLE weather_information (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(255),
    temperature DOUBLE,
    humidity DOUBLE,
    rainfall DOUBLE,
    weather_condition VARCHAR(100),
    farm_id BIGINT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_weather_farm FOREIGN KEY (farm_id) REFERENCES farms(id) ON DELETE SET NULL
);
