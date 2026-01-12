package com.cloud.auth.domain.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID uuid,
        String email,
        String token
) {
}