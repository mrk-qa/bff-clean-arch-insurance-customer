package com.poc.bff_clean_arch_insurance_customer.adapter.in.web;

import com.poc.bff_clean_arch_insurance_customer.application.usecase.GetCustomerDashboardUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerDashboardController {

    private final GetCustomerDashboardUseCase getCustomerDashboardUseCase;
    private final CustomerDashboardMapper customerDashboardMapper;

    public CustomerDashboardController(
            GetCustomerDashboardUseCase getCustomerDashboardUseCase,
            CustomerDashboardMapper customerDashboardMapper
    ) {
        this.getCustomerDashboardUseCase = getCustomerDashboardUseCase;
        this.customerDashboardMapper = customerDashboardMapper;
    }

    @GetMapping("/{customerId}/dashboard")
    public CustomerDashboardResponse getDashboard(
            @PathVariable Long customerId
    ) {

        var dashboard =
                getCustomerDashboardUseCase.execute(customerId);

        return customerDashboardMapper.toResponse(dashboard);
    }
}