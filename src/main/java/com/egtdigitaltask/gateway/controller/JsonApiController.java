package com.egtdigitaltask.gateway.controller;

import com.egtdigitaltask.gateway.model.dto.CurrentExchangeRateRequest;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistoryResponse;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistoryRequest;
import com.egtdigitaltask.gateway.model.dto.CurrentExchangeRateResponse;
import com.egtdigitaltask.gateway.service.ExchangeRateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/json_api")
public class JsonApiController
{
    private final ExchangeRateService exchangeRateService;

    private static final String JSON_API_EXT_SERVICE_NAME = "EXT_SERVICE_2";

    @Autowired
    public JsonApiController(ExchangeRateService service)
    {
        this.exchangeRateService = service;
    }

    @PostMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrentExchangeRateResponse> getCurrentExchangeRate(@Valid @RequestBody CurrentExchangeRateRequest body)
    {
        CurrentExchangeRateResponse response = exchangeRateService.getCurrentExchangeRate(JSON_API_EXT_SERVICE_NAME,
                                                                                          body.getRequestId(),
                                                                                          body.getClient(),
                                                                                          body.getCurrency(),
                                                                                          body.getTimestamp());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExchangeRateHistoryResponse> getExchangeRateHistory(@Valid @RequestBody ExchangeRateHistoryRequest body)
    {
        ExchangeRateHistoryResponse response = exchangeRateService.getExchangeRateHistory(JSON_API_EXT_SERVICE_NAME,
                                                                                          body.getRequestId(),
                                                                                          body.getClient(),
                                                                                          body.getCurrency(),
                                                                                          body.getPeriod(),
                                                                                          body.getTimestamp());
        return ResponseEntity.ok().body(response);
    }

}

