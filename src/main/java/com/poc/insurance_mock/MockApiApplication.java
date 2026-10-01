package com.poc.insurance_mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@SpringBootApplication
public class MockApiApplication {

    public static void main(String[] args) {

        SpringApplication app =
                new SpringApplication(MockApiApplication.class);

        app.setAdditionalProfiles("mock");

        app.run(args);
    }
}