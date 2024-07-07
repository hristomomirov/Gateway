package com.egtdigitaltask.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FixerRestClientConfig
{
    @Bean
    public RestClient fixerRestClient()
    {
        return RestClient.builder()
                         .baseUrl("http://data.fixer.io/api/")  // Default base URL
                         .build();
    }
}
