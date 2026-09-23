package com.poc.bff_clean_arch_insurance_customer.domain.model;

import java.util.List;

public record Policy(
        Long id,
        String policyNumber,
        String status,
        List<String> coverage,
        Long vehicleId
) {
}