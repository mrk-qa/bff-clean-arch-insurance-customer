package com.poc.bff_clean_arch_insurance_customer.adapter.in.web;

import java.util.List;

public record CustomerDashboardResponse(
        CustomerResponse customer,
        List<PolicyResponse> policies,
        List<VehicleResponse> vehicles
) {

    public record CustomerResponse(
            Long id,
            String name
    ) {
    }

    public record PolicyResponse(
            Long id,
            String policyNumber,
            String status,
            List<String> coverage,
            Long vehicleId
    ) {
    }

    public record VehicleResponse(
            Long id,
            String brand,
            String model,
            Integer year
    ) {
    }
}