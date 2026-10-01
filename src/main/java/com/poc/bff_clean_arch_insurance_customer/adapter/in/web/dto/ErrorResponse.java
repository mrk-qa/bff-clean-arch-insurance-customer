package com.poc.bff_clean_arch_insurance_customer.adapter.in.web.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}