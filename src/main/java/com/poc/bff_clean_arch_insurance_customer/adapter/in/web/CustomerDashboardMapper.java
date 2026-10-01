package com.poc.bff_clean_arch_insurance_customer.adapter.in.web;

import com.poc.bff_clean_arch_insurance_customer.domain.model.CustomerDashboard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDashboardMapper {

    public CustomerDashboardResponse toResponse(
            CustomerDashboard dashboard
    ) {

        var customer = new CustomerDashboardResponse.CustomerResponse(
                dashboard.customer().id(),
                dashboard.customer().name()
        );

        List<CustomerDashboardResponse.PolicyResponse> policies =
                dashboard.policies()
                        .stream()
                        .map(policy -> new CustomerDashboardResponse.PolicyResponse(
                                policy.id(),
                                policy.policyNumber(),
                                policy.status(),
                                policy.coverage(),
                                policy.vehicleId()
                        ))
                        .toList();

        List<CustomerDashboardResponse.VehicleResponse> vehicles =
                dashboard.vehicles()
                        .stream()
                        .map(vehicle -> new CustomerDashboardResponse.VehicleResponse(
                                vehicle.id(),
                                vehicle.brand(),
                                vehicle.model(),
                                vehicle.year()
                        ))
                        .toList();

        return new CustomerDashboardResponse(
                customer,
                policies,
                vehicles
        );
    }
}