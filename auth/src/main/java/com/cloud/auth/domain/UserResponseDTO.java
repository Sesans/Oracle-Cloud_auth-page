package com.cloud.auth.domain;

import java.util.UUID;

public record UserResponseDTO(
        UUID uuid,
        String email
) {
}