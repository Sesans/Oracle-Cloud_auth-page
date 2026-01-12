package com.cloud.auth.exception;

import java.time.LocalDateTime;

public record ErrorDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}
