package com.poc.bff_clean_arch_insurance_customer.domain.port;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Vehicle;

public interface VehicleApi {

    Vehicle findVehicle(Long vehicleId);
}