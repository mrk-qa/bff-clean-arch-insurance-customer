package com.poc.bff_clean_arch_insurance_customer.domain.model;

public record Vehicle(
        Long id,
        String brand,
        String model,
        Integer year
) {
}