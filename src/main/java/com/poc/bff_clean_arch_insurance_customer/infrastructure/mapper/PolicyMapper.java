package com.poc.bff_clean_arch_insurance_customer.infrastructure.mapper;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Policy;
import com.poc.bff_clean_arch_insurance_customer.infrastructure.dto.PolicyApiResponse;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapper {

    public Policy toDomain(PolicyApiResponse response) {

        return new Policy(
                response.id(),
                response.policyNumber(),
                response.status(),
                response.coverage(),
                response.vehicleId()
        );
    }
}