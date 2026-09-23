package com.poc.bff_clean_arch_insurance_customer.domain.port;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Policy;

import java.util.List;

public interface PolicyApi {

    List<Policy> findPoliciesByCustomer(Long customerId);
}