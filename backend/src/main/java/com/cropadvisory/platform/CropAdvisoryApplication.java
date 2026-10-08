package com.cropadvisory.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Crop Advisory and Farming Guidance Platform.
 *
 * <p>This application provides REST APIs for crop recommendations, farming guidance,
 * disease/pest management, expert consultation, and agricultural notifications.</p>
 */
@SpringBootApplication
@EnableScheduling
public class CropAdvisoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CropAdvisoryApplication.class, args);
    }
}
