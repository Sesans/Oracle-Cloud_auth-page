package com.cloud.auth.domain.dto;

import org.springframework.http.ResponseCookie;

public record AuthResponseWrapper(UserResponseDTO responseDTO, ResponseCookie cookie) {
}
