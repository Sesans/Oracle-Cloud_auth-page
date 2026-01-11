package com.cloud.auth.domain;

public record UserLoginDTO(
        String email,
        String password
) {
}
