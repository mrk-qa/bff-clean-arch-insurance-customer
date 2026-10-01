package com.poc.insurance_mock.controller;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Vehicle;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/vehicles")
public class MockVehicleController {

    @GetMapping("/{vehicleId}")
    public Vehicle findVehicle(
            @PathVariable Long vehicleId
    ) {

        return switch (vehicleId.intValue()) {

            case 101 -> new Vehicle(
                    101L,
                    "Toyota",
                    "Corolla",
                    2025
            );

            case 102 -> new Vehicle(
                    102L,
                    "Honda",
                    "Civic",
                    2024
            );

            default -> throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Vehicle not found"
            );
        };
    }
}