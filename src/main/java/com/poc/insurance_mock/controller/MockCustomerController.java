package com.poc.insurance_mock.controller;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Customer;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/customers")
public class MockCustomerController {

    @GetMapping("/{id}")
    public Customer findCustomer(@PathVariable Long id) {

        if (id != 1L) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer " + id + " not found"
            );
        }

        return new Customer(
                1L,
                "João da Silva"
        );
    }
}