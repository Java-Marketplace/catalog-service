package com.jmp.template.dto.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp, Integer status, String error, String message
) {
}
