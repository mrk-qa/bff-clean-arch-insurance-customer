package com.poc.bff_clean_arch_insurance_customer.domain.model;

import java.util.List;

public record CustomerDashboard(
        Customer customer,
        List<Policy> policies,
        List<Vehicle> vehicles
) {
}