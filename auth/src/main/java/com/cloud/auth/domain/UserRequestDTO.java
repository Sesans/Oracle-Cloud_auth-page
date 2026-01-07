package com.cloud.auth.domain;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String password
) {
}