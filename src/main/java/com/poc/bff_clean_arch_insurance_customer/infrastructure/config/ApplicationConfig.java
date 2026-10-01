package com.poc.bff_clean_arch_insurance_customer.infrastructure.config;

import com.poc.bff_clean_arch_insurance_customer.application.usecase.GetCustomerDashboardUseCase;
import com.poc.bff_clean_arch_insurance_customer.domain.port.CustomerApi;
import com.poc.bff_clean_arch_insurance_customer.domain.port.PolicyApi;
import com.poc.bff_clean_arch_insurance_customer.domain.port.VehicleApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public GetCustomerDashboardUseCase getCustomerDashboardUseCase(
            CustomerApi customerApi,
            PolicyApi policyApi,
            VehicleApi vehicleApi
    ) {
        return new GetCustomerDashboardUseCase(
                customerApi,
                policyApi,
                vehicleApi
        );
    }
}