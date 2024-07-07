package com.egtdigitaltask.gateway.controller;

import com.egtdigitaltask.gateway.model.dto.*;
import com.egtdigitaltask.gateway.service.ExchangeRateService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/xml_api")
public class XmlApiController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlApiController.class);
    private final ExchangeRateService exchangeRateService;

    private static final String XML_API_EXT_SERVICE_NAME = "EXT_SERVICE_1";

    @Autowired
    public XmlApiController(ExchangeRateService service)
    {
        this.exchangeRateService = service;
    }

    @PostMapping(path = "/command", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ExchangeRateResponse> getExchangeRateHistory(@Valid @RequestBody CommandRequest request)
    {
        if (request == null)
        {
            return ResponseEntity.ok().build();
        }

        long timestamp = Instant.now().getEpochSecond();
        if (request.getGet() != null)
        {
            Get get = request.getGet();
            CurrentExchangeRateResponse response = exchangeRateService.getCurrentExchangeRate(XML_API_EXT_SERVICE_NAME,
                                                                                              request.getId(),
                                                                                              get.getConsumer(),
                                                                                              get.getCurrency(),
                                                                                              timestamp);
            return ResponseEntity.ok().body(response);
        }
        else if (request.getHistory() != null)
        {
            History history = request.getHistory();
            ExchangeRateHistoryResponse response = exchangeRateService.getExchangeRateHistory(XML_API_EXT_SERVICE_NAME,
                                                                                              request.getId(),
                                                                                              history.getConsumer(),
                                                                                              history.getCurrency(),
                                                                                              history.getPeriod(),
                                                                                              timestamp);
            return ResponseEntity.ok().body(response);
        }
        else
        {
            LOGGER.error("Cannot process get current and get history with single request. requestId=" + request.getId());
            throw new IllegalArgumentException("Cannot process get current and get history with single request");
        }
    }
}
