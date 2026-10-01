package com.poc.bff_clean_arch_insurance_customer.application.usecase;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Customer;
import com.poc.bff_clean_arch_insurance_customer.domain.model.CustomerDashboard;
import com.poc.bff_clean_arch_insurance_customer.domain.model.Policy;
import com.poc.bff_clean_arch_insurance_customer.domain.model.Vehicle;
import com.poc.bff_clean_arch_insurance_customer.domain.port.CustomerApi;
import com.poc.bff_clean_arch_insurance_customer.domain.port.PolicyApi;
import com.poc.bff_clean_arch_insurance_customer.domain.port.VehicleApi;

import java.util.List;

public class GetCustomerDashboardUseCase {

    private final CustomerApi customerApi;
    private final PolicyApi policyApi;
    private final VehicleApi vehicleApi;

    public GetCustomerDashboardUseCase(
            CustomerApi customerApi,
            PolicyApi policyApi,
            VehicleApi vehicleApi
    ) {
        this.customerApi = customerApi;
        this.policyApi = policyApi;
        this.vehicleApi = vehicleApi;
    }

    public CustomerDashboard execute(Long customerId) {

        Customer customer = customerApi.findCustomer(customerId);

        List<Policy> policies =
                policyApi.findPoliciesByCustomer(customerId);

        List<Vehicle> vehicles = policies.stream()
                .map(policy -> vehicleApi.findVehicle(policy.vehicleId()))
                .toList();

        return new CustomerDashboard(
                customer,
                policies,
                vehicles
        );
    }
}