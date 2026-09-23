package com.poc.bff_clean_arch_insurance_customer.domain.port;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Customer;

public interface CustomerApi {

    Customer findCustomer(Long customerId);
}