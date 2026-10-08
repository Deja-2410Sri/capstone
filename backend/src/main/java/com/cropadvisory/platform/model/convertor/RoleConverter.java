package com.cropadvisory.platform.model.converter;

import com.cropadvisory.platform.model.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) return null;

        return switch (role) {
            case ROLE_FARMER -> "role_farmer";
            case ROLE_EXPERT -> "role_expert";
            case ROLE_ADMIN -> "role_admin";
        };
    }

    @Override
    public Role convertToEntityAttribute(String value) {
        if (value == null) return null;

        return switch (value) {
            case "role_farmer" -> Role.ROLE_FARMER;
            case "role_expert" -> Role.ROLE_EXPERT;
            case "role_admin" -> Role.ROLE_ADMIN;
            default -> throw new IllegalArgumentException("Unknown role: " + value);
        };
    }
}