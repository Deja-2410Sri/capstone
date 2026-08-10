1. Title

Crop Advisory & Farming Guidance Platform for Farmers

2. Domain

Agriculture Technology (AgriTech)

3. Who is the user?
Farmer – Checks crop information, receives farming guidance, and gets recommendations.
Agricultural Expert – Provides expert advice and manages crop-related recommendations.
Admin – Manages users, crop information, and platform content.
4. What problem are we solving?

Farmers often face difficulties in choosing suitable crops, identifying suitable farming practices, and getting timely agricultural guidance. They may depend on local sources or traditional knowledge, which may not always provide accurate or timely information. For example, a farmer may plant a crop that is not suitable for the soil or season and face poor yield. This platform provides farmers with easily accessible crop recommendations and farming guidance in one place.

5. Proposed Solution

The application will provide:

User Registration & Login – Secure farmer and expert accounts.
Crop Recommendation – Suggest suitable crops based on soil, season, and location.
Crop Information – Provide details about crops, required soil, season, and basic cultivation practices.
Farming Guidance – Provide information about sowing, irrigation, fertilizers, and harvesting.
Disease/Pest Information – Help farmers identify common crop diseases and pests.
Expert Consultation – Allow farmers to submit questions and receive guidance from agricultural experts.
Notifications – Send important farming tips and alerts.
Admin Dashboard – Manage users, crops, recommendations, and expert content.
6. Core Entities / Database Tables

Minimum 5 tables:

Users – User ID, name, email, phone, password, role
Farmers – Farmer ID, user ID, location, soil type, farm size
Crops – Crop ID, crop name, season, soil type, description
Crop Recommendations – Recommendation ID, farmer ID, crop ID, reason
Farming Guidelines – Guideline ID, crop ID, sowing, irrigation, fertilizer, harvesting
Disease/Pests – Disease ID, crop ID, disease name, symptoms, prevention
Expert Advice – Advice ID, farmer ID, expert ID, question, response
Notifications – Notification ID, user ID, message, date
7. User Roles & Permissions
Role	Permissions
Farmer	Register/login, enter farm details, view crop recommendations, view farming guidelines, ask experts, receive notifications
Agricultural Expert	Login, view farmer questions, provide advice, update farming guidance
Admin	Manage users, crops, guidelines, diseases, experts, and system content
8. Success Criteria
A farmer should be able to register and enter farm details within 2 minutes.
A farmer should receive crop recommendations within 10 seconds.
Users should be able to easily search and view crop information.
Farmers should be able to submit questions to experts successfully.
Experts should be able to respond to farmer queries.
Admin should be able to add, update, and delete crop information.
The system should provide reliable and easy-to-understand farming guidance.
9. Out of Scope

The following will not be included in the initial version:

Online buying and selling of crops.
Online purchase of fertilizers, seeds, or pesticides.
Direct weather-station hardware integration.
Drone-based crop monitoring.
Automatic IoT-based soil monitoring.
Online banking or payment system.
Physical delivery of agricultural products.
Fully automated disease diagnosis using advanced computer vision.
10. Chosen Track

Java – Spring Boot

Technology Stack:

Frontend: HTML, CSS, JavaScript / React
Backend: Java Spring Boot
Database: MySQL
API: REST API
Tools: IntelliJ IDEA / VS Code, Postman, Git & GitHub

