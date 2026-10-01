package com.poc.bff_clean_arch_insurance_customer.infrastructure.client;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Customer;
import com.poc.bff_clean_arch_insurance_customer.domain.port.CustomerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.BadRequestException;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.ResourceNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class CustomerApiClient implements CustomerApi {

    private final RestClient restClient;

    public CustomerApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${external.customer-api.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Customer findCustomer(Long customerId) {

        try {

            return restClient
                    .get()
                    .uri("/customers/{id}", customerId)
                    .retrieve()
                    .body(Customer.class);

        } catch (HttpClientErrorException.BadRequest exception) {

            throw new BadRequestException(
                    "Customer API rejected the request."
            );

        } catch (HttpClientErrorException.NotFound exception) {

            throw new ResourceNotFoundException(
                    "Customer " + customerId + " not found."
            );
        }
    }
}