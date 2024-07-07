package com.egtdigitaltask.gateway.service;

import com.egtdigitaltask.gateway.model.ExchangeRate;
import com.egtdigitaltask.gateway.model.RequestData;
import com.egtdigitaltask.gateway.model.dto.CurrentExchangeRateResponse;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistory;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistoryResponse;
import com.egtdigitaltask.gateway.repository.ExchangeRateRepository;
import com.egtdigitaltask.gateway.repository.RequestRepository;
import com.egtdigitaltask.gateway.utill.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService
{
    private final ExchangeRateRepository exchangeRateRepository;

    private final RequestRepository requestRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateService.class);


    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               RequestRepository requestRepository)
    {
        this.exchangeRateRepository = exchangeRateRepository;
        this.requestRepository = requestRepository;
    }

    public ExchangeRateHistoryResponse getExchangeRateHistory(String serviceName,
                                                              String requestId,
                                                              String client,
                                                              String currency,
                                                              long period,
                                                              long timestamp)
    {
        //validate if request with this id does not exist
        LOGGER.info("Validating request with requestId=" + requestId);
        Optional<RequestData> optionalRequestData = requestRepository.findByRequestId(requestId);
        Validator.validateUniqueRequestId(optionalRequestData);

        //create request entity
        RequestData requestData = new RequestData(serviceName, requestId, timestamp, client);

        List<ExchangeRate> exchangeRates = insertRequestAndGetExchangeRates(requestData, currency, period);

        if (exchangeRates.isEmpty())
        {
            LOGGER.warn("Could not retrieve current exchange rates for request with requestId=" + requestId
                                                                            + " and baseCurrency= " + currency);
            return new ExchangeRateHistoryResponse(currency, null);
        }
        return new ExchangeRateHistoryResponse(currency, generateExchangeRateHistory(exchangeRates));
    }

    @Cacheable // redis
    public CurrentExchangeRateResponse getCurrentExchangeRate(String serviceName,
                                                              String requestId,
                                                              String client,
                                                              String currency,
                                                              long timestamp)
    {
        //validate if request with this id does not exist
        LOGGER.info("Validating request with requestId=" + requestId);
        Optional<RequestData> optionalRequestData = requestRepository.findByRequestId(requestId);
        Validator.validateUniqueRequestId(optionalRequestData);

        //create request entity
        RequestData requestData = new RequestData(serviceName, requestId, timestamp, client);

        //to get the latest exchange rates we query for the rates inserted in the last hour
        long oneHourPeriod = 60 * 60;
        List<ExchangeRate> exchangeRates = insertRequestAndGetExchangeRates(requestData, currency, oneHourPeriod);

        if (exchangeRates.isEmpty())
        {
            LOGGER.warn("Could not retrieve current exchange rates for request with requestId=" + requestId
                        + " and baseCurrency= " + currency);
            return new CurrentExchangeRateResponse(currency, 0, null);
        }

        TreeMap<String, Double> tickerToRate = exchangeRates.stream()
                                                        .collect(Collectors.toMap(er -> er.getTarget().getTicker(),
                                                                                  ExchangeRate::getRate,
                                                                                  (a, b) -> a,
                                                                                  TreeMap::new));
        long currentRatesTimestamp = exchangeRates.get(0).getTimestamp();
        return new CurrentExchangeRateResponse(currency, currentRatesTimestamp, tickerToRate);
    }

    @Transactional
    private List<ExchangeRate> insertRequestAndGetExchangeRates(RequestData requestData, String currency, long period)
    {
        //insert request in DB and get history in transaction
        requestRepository.save(requestData);
        //we try to get the exchange rate per base currency between now and a point in history in UTC time
        //seconds * minutes * hours
        long startTime = Instant.now().minusSeconds(60 * 60 * period).getEpochSecond();
        long endTime = Instant.now().getEpochSecond();
        return exchangeRateRepository.findByTimestampBetweenAndBaseTickerOrderByTargetTickerAsc(startTime,
                                                                                                endTime,
                                                                                                currency);
    }

    private List<ExchangeRateHistory> generateExchangeRateHistory(List<ExchangeRate> exchangeRates)
    {
        TreeMap<Long, Map<String, Double>> timestampToTickerToRate = new TreeMap<>();
        for (ExchangeRate rate : exchangeRates)
        {
            if (!timestampToTickerToRate.containsKey(rate.getTimestamp()))
            {
                timestampToTickerToRate.put(rate.getTimestamp(), new TreeMap<>());
            }

            timestampToTickerToRate.get(rate.getTimestamp()).put(rate.getTarget().getTicker(), rate.getRate());
        }

        return timestampToTickerToRate.entrySet().stream()
                                      .map(e -> new ExchangeRateHistory(e.getKey(),
                                                                        e.getValue()))
                                      .toList();
    }
}