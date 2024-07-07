package com.egtdigitaltask.gateway.service;

import com.egtdigitaltask.gateway.model.dto.FixerCurrencyNamesResponse;
import com.egtdigitaltask.gateway.model.dto.FixerExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FixerWebClient
{
    @Value("${fixerIo.accessKey}")
    private String fixerAccessKey;

    private final RestClient restClient;

    @Autowired
    public FixerWebClient(RestClient restClient)
    {
        this.restClient = restClient;
    }

    public FixerExchangeRateResponse getFixerExchangeRate(String base, String currencies)
    {
        return restClient.get()
                         .uri(uriBuilder -> uriBuilder.path("/latest")
                                                      .queryParam("base", base)
                                                      .queryParam("symbols", currencies)
                                                      .queryParam("access_key", fixerAccessKey)
                                                      .build())
                         .retrieve()
                         .body(FixerExchangeRateResponse.class);
    }

    public FixerCurrencyNamesResponse getCurrencyNames()
    {
        //todo try-catch
        return restClient.get()
                         .uri(uriBuilder -> uriBuilder.path("/symbols")
                                                      .queryParam("access_key", fixerAccessKey)
                                                      .build())
                         .retrieve().body(FixerCurrencyNamesResponse.class);
    }
}
