package com.poc.bff_clean_arch_insurance_customer.infrastructure.client;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Vehicle;
import com.poc.bff_clean_arch_insurance_customer.domain.port.VehicleApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.BadRequestException;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.ResourceNotFoundException;

@Component
public class VehicleApiClient implements VehicleApi {

    private final RestClient restClient;

    public VehicleApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${external.vehicle-api.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Vehicle findVehicle(Long vehicleId) {

        try {

            return restClient
                    .get()
                    .uri("/vehicles/{id}", vehicleId)
                    .retrieve()
                    .body(Vehicle.class);

        } catch (HttpClientErrorException.BadRequest exception) {

            throw new BadRequestException(
                    "Vehicle API rejected the request."
            );

        } catch (HttpClientErrorException.NotFound exception) {

            throw new ResourceNotFoundException(
                    "Vehicle " + vehicleId + " not found."
            );
        }
    }
}