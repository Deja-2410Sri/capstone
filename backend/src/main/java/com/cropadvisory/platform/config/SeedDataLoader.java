package com.cropadvisory.platform.config;

import com.cropadvisory.platform.model.entity.*;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Development seed data loader.
 *
 * <p>Creates sample data when SEED_ADMIN=true. Creates admin, farmer, expert accounts
 * and sample crops, guidelines, and disease records.</p>
 */
@Component
public class SeedDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final ExpertProfileRepository expertProfileRepository;
    private final CropRepository cropRepository;
    private final FarmingGuidelineRepository guidelineRepository;
    private final DiseasePestRepository diseasePestRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@cropadvisory.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.seed:false}")
    private boolean seedEnabled;

    public SeedDataLoader(UserRepository userRepository,
                          FarmerProfileRepository farmerProfileRepository,
                          ExpertProfileRepository expertProfileRepository,
                          CropRepository cropRepository,
                          FarmingGuidelineRepository guidelineRepository,
                          DiseasePestRepository diseasePestRepository,
                          MarketPriceRepository marketPriceRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.expertProfileRepository = expertProfileRepository;
        this.cropRepository = cropRepository;
        this.guidelineRepository = guidelineRepository;
        this.diseasePestRepository = diseasePestRepository;
        this.marketPriceRepository = marketPriceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            log.info("Seed data loading is disabled");
            return;
        }

        if (userRepository.count() > 0) {
            log.info("Database already contains data. Skipping seed.");
            return;
        }

        log.info("Loading seed data...");

        User admin = createAdmin();
        User farmer = createFarmer();
        User expert = createExpert();
        List<Crop> crops = createCrops();
        createGuidelines(crops);
        createDiseases(crops);
        createMarketPrices(crops);

        log.info("Seed data loaded successfully");
    }

    private User createAdmin() {
        User admin = User.builder()
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .firstName("Admin")
                .lastName("User")
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();
        admin = userRepository.save(admin);
        log.info("Admin account created: {}", adminEmail);
        return admin;
    }

    private User createFarmer() {
        User farmer = User.builder()
                .email("farmer@example.com")
                .passwordHash(passwordEncoder.encode("farmer123"))
                .firstName("Ramesh")
                .lastName("Kumar")
                .phone("9876543210")
                .role(Role.ROLE_FARMER)
                .enabled(true)
                .build();
        farmer = userRepository.save(farmer);

        FarmerProfile profile = FarmerProfile.builder()
                .user(farmer)
                .location("Punjab")
                .address("Village Hoshiarpur")
                .district("Hoshiarpur")
                .state("Punjab")
                .postalCode("146001")
                .soilType("Loamy")
                .farmSize(5.0)
                .build();
        farmerProfileRepository.save(profile);
        log.info("Farmer account created: farmer@example.com");
        return farmer;
    }

    private User createExpert() {
        User expert = User.builder()
                .email("expert@example.com")
                .passwordHash(passwordEncoder.encode("expert123"))
                .firstName("Dr. Suresh")
                .lastName("Patel")
                .phone("9876543211")
                .role(Role.ROLE_EXPERT)
                .enabled(true)
                .build();
        expert = userRepository.save(expert);

        ExpertProfile profile = ExpertProfile.builder()
                .user(expert)
                .specialization("Crop Science")
                .qualification("Ph.D. Agriculture")
                .experience("15 years")
                .contact("9876543211")
                .build();
        expertProfileRepository.save(profile);
        log.info("Expert account created: expert@example.com");
        return expert;
    }

    private List<Crop> createCrops() {
        List<Crop> crops = List.of(
                Crop.builder().cropName("Rice").category("Cereal").season("Kharif").soilType("Clay").waterRequirement("High").temperatureRange("20-35").description("Staple food crop grown in wet conditions").active(true).build(),
                Crop.builder().cropName("Wheat").category("Cereal").season("Rabi").soilType("Loamy").waterRequirement("Medium").temperatureRange("10-25").description("Major winter season cereal crop").active(true).build(),
                Crop.builder().cropName("Maize").category("Cereal").season("Kharif").soilType("Loamy").waterRequirement("Medium").temperatureRange("20-30").description("Versatile cereal crop used for food and feed").active(true).build(),
                Crop.builder().cropName("Cotton").category("Fiber").season("Kharif").soilType("Black").waterRequirement("Medium").temperatureRange("25-35").description("Important fiber crop").active(true).build(),
                Crop.builder().cropName("Groundnut").category("Oilseed").season("Kharif").soilType("Sandy").waterRequirement("Low").temperatureRange("25-30").description("Major oilseed crop").active(true).build(),
                Crop.builder().cropName("Tomato").category("Vegetable").season("Both").soilType("Loamy").waterRequirement("Medium").temperatureRange("20-30").description("Popular vegetable crop").active(true).build(),
                Crop.builder().cropName("Sugarcane").category("Cash Crop").season("Annual").soilType("Clay").waterRequirement("High").temperatureRange("20-40").description("Major sugar producing crop").active(true).build()
        );
        return cropRepository.saveAll(crops);
    }

    private void createGuidelines(List<Crop> crops) {
        for (Crop crop : crops) {
            FarmingGuideline guideline = FarmingGuideline.builder()
                    .crop(crop)
                    .sowingMethod("Standard sowing method for " + crop.getCropName())
                    .irrigation("Regular irrigation required based on soil moisture")
                    .fertilizer("NPK based fertilizer application recommended")
                    .pestManagement("Integrated pest management approach recommended")
                    .harvesting("Harvest at optimal maturity stage")
                    .generalTips("Monitor crop health regularly and maintain proper spacing")
                    .build();
            guidelineRepository.save(guideline);
        }
    }

    private void createDiseases(List<Crop> crops) {
        for (Crop crop : crops) {
            DiseasePest disease = DiseasePest.builder()
                    .crop(crop)
                    .diseaseName("Common leaf spot")
                    .symptoms("Brown spots on leaves")
                    .causes("Fungal infection due to high humidity")
                    .prevention("Ensure proper spacing and ventilation")
                    .treatment("Apply fungicide as recommended")
                    .build();
            diseasePestRepository.save(disease);
        }
    }

    private void createMarketPrices(List<Crop> crops) {
        for (Crop crop : crops) {
            MarketPrice price = MarketPrice.builder()
                    .crop(crop)
                    .marketName("Local Mandi")
                    .location("Punjab")
                    .price(BigDecimal.valueOf(2000 + (int)(Math.random() * 3000)))
                    .unit("quintal")
                    .recordedDate(LocalDate.now())
                    .build();
            marketPriceRepository.save(price);
        }
    }
}
