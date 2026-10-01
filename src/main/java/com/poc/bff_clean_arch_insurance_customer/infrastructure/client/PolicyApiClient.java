package com.poc.bff_clean_arch_insurance_customer.infrastructure.client;

import com.poc.bff_clean_arch_insurance_customer.domain.model.Policy;
import com.poc.bff_clean_arch_insurance_customer.domain.port.PolicyApi;
import com.poc.bff_clean_arch_insurance_customer.infrastructure.dto.PolicyApiResponse;
import com.poc.bff_clean_arch_insurance_customer.infrastructure.mapper.PolicyMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.BadRequestException;
import com.poc.bff_clean_arch_insurance_customer.domain.exception.ResourceNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Component
public class PolicyApiClient implements PolicyApi {

    private final RestClient restClient;
    private final PolicyMapper policyMapper;

    public PolicyApiClient(
            RestClient.Builder restClientBuilder,
            PolicyMapper policyMapper,
            @Value("${external.policy-api.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        this.policyMapper = policyMapper;
    }

    @Override
    public List<Policy> findPoliciesByCustomer(Long customerId) {

        try {

            List<PolicyApiResponse> responses = restClient
                    .get()
                    .uri("/policies/customer/{id}", customerId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<PolicyApiResponse>>() {});

            return responses.stream()
                    .map(policyMapper::toDomain)
                    .toList();

        } catch (HttpClientErrorException.BadRequest exception) {

            throw new BadRequestException(
                    "Policy API rejected the request."
            );

        } catch (HttpClientErrorException.NotFound exception) {

            throw new ResourceNotFoundException(
                    "Policies for customer " + customerId + " not found."
            );
        }
    }
}