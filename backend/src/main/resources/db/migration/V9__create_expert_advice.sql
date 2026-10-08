CREATE TABLE expert_advice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    expert_id BIGINT,
    question TEXT NOT NULL,
    response TEXT,
    status VARCHAR(20) DEFAULT 'OPEN',
    asked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP NULL,
    CONSTRAINT fk_advice_farmer FOREIGN KEY (farmer_id) REFERENCES farmer_profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_advice_expert FOREIGN KEY (expert_id) REFERENCES expert_profiles(id) ON DELETE SET NULL
);

CREATE INDEX idx_advice_farmer_id ON expert_advice(farmer_id);
CREATE INDEX idx_advice_expert_id ON expert_advice(expert_id);
CREATE INDEX idx_advice_status ON expert_advice(status);
