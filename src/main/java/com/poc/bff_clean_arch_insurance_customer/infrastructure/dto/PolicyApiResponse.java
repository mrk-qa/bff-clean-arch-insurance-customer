package com.poc.bff_clean_arch_insurance_customer.infrastructure.dto;

import java.util.List;

public record PolicyApiResponse(
        Long id,
        String policyNumber,
        String status,
        List<String> coverage,
        Long vehicleId
) {
}