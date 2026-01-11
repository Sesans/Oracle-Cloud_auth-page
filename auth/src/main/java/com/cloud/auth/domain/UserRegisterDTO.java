package com.cloud.auth.domain;

public record UserRegisterDTO(
        String firstName,
        String lastName,
        String email,
        String password
) {
}