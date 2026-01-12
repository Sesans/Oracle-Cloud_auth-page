package com.cloud.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginDTO(
        @NotBlank
        String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[.@#$%^&+=!]).{8,}$",
                message = "Password must be at least 8 characters long, including 1 uppercase, 1 lowercase, a number and a special character ")
        String password
) {
}
