package com.poc.insurance_mock.controller;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Policy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/policies")
public class MockPolicyController {

    @GetMapping("/customer/{customerId}")
    public List<Policy> findPoliciesByCustomer(
            @PathVariable Long customerId
    ) {

        List<Policy> policies;

        if (customerId.equals(1L)) {

            policies = List.of(
                    new Policy(
                            10L,
                            "POL-2026-001",
                            "ACTIVE",
                            List.of(
                                    "COLLISION",
                                    "THEFT"
                            ),
                            101L
                    ),
                    new Policy(
                            11L,
                            "POL-2026-002",
                            "ACTIVE",
                            List.of(
                                    "THIRD_PARTY"
                            ),
                            102L
                    )
            );

        } else {
            policies = List.of();
        }

        if (policies.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No policies found for customer " + customerId
            );
        }

        return policies;
    }
}