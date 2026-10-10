ALTER TABLE users
ADD COLUMN verification_otp_hash VARCHAR(255) NULL,
ADD COLUMN verification_otp_expires_at DATETIME NULL,
ADD COLUMN verification_otp_attempts INT NOT NULL DEFAULT 0,
ADD COLUMN verification_otp_last_sent_at DATETIME NULL;
