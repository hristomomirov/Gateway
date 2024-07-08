package com.egtdigitaltask.gateway.service;

import com.egtdigitaltask.gateway.model.ExchangeRate;
import com.egtdigitaltask.gateway.model.RequestData;
import com.egtdigitaltask.gateway.model.dto.CurrentExchangeRateResponse;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistory;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistoryResponse;
import com.egtdigitaltask.gateway.repository.ExchangeRateRepository;
import com.egtdigitaltask.gateway.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RabbitMqProducer rabbitMqProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateService.class);


    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               RequestRepository requestRepository,
                               RabbitMqProducer rabbitMqProducer)
    {
        this.exchangeRateRepository = exchangeRateRepository;
        this.requestRepository = requestRepository;
        this.rabbitMqProducer = rabbitMqProducer;
    }

    public ExchangeRateHistoryResponse getExchangeRateHistory(String serviceName,
                                                              String requestId,
                                                              String client,
                                                              String currency,
                                                              long period,
                                                              long timestamp)
    {
        validateUniqueRequestId(requestId);
        RequestData requestData = new RequestData(serviceName, requestId, timestamp, client);
        List<ExchangeRate> exchangeRates = insertRequestAndGetExchangeRatesHistory(requestData, currency, period);

        rabbitMqProducer.sendMessage(requestData);

        if (exchangeRates.isEmpty())
        {
            LOGGER.warn("Could not retrieve current exchange rates for request with requestId=" + requestId
                                                                                + " and baseCurrency= " + currency);
            return new ExchangeRateHistoryResponse(currency, null);
        }
        return new ExchangeRateHistoryResponse(currency, generateExchangeRateHistory(exchangeRates));
    }

    public CurrentExchangeRateResponse getCurrentExchangeRate(String serviceName,
                                                              String requestId,
                                                              String client,
                                                              String currency,
                                                              long timestamp)
    {
        validateUniqueRequestId(requestId);
        RequestData requestData = new RequestData(serviceName, requestId, timestamp, client);

        //to get the latest exchange rates we query for the rates inserted in the last hour
        List<ExchangeRate> exchangeRates = insertRequestAndGetLatestExchangeRates(requestData, currency);

        rabbitMqProducer.sendMessage(requestData);
        LOGGER.info("Message sent to queue");

        if (exchangeRates.isEmpty())
        {
            LOGGER.warn("Could not retrieve current exchange rates for request with requestId=" + requestId
                                                                                + " and baseCurrency= " + currency);
            return new CurrentExchangeRateResponse(currency, 0, null);
        }
        return generateCurrentExchangeRate(exchangeRates, currency);
    }

    private static CurrentExchangeRateResponse generateCurrentExchangeRate(List<ExchangeRate> exchangeRates,
                                                                           String currency)
    {
        TreeMap<String, Double> tickerToRate = exchangeRates.stream()
                                                            .collect(Collectors.toMap(er -> er.getTarget().getTicker(),
                                                                                      ExchangeRate::getRate,
                                                                                      (a, b) -> a,
                                                                                      TreeMap::new));

        long currentRatesTimestamp = exchangeRates.get(0).getTimestamp();
        return new CurrentExchangeRateResponse(currency, currentRatesTimestamp, tickerToRate);
    }

    @Transactional
    public List<ExchangeRate> insertRequestAndGetExchangeRatesHistory(RequestData requestData, String currency, long period)
    {
        requestRepository.save(requestData);
        //we try to get the exchange rate per base currency between now and a point in history in UTC time
        //seconds * minutes * hours
        long startTime = Instant.now().minusSeconds(60 * 60 * period).getEpochSecond();
        long endTime = Instant.now().getEpochSecond();
        return exchangeRateRepository.findByTimestampBetweenAndBaseTickerOrderByTargetTickerAsc(startTime,
                                                                                                endTime,
                                                                                                currency);
    }
    @Transactional
    public List<ExchangeRate> insertRequestAndGetLatestExchangeRates(RequestData requestData, String currency)
    {
        requestRepository.save(requestData);
        return exchangeRateRepository.findRecentRates(currency);
    }

    private static List<ExchangeRateHistory> generateExchangeRateHistory(List<ExchangeRate> exchangeRates)
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

    private void validateUniqueRequestId(String requestId)
    {
        //validate if request with this id does not exist
        LOGGER.info("Validating request with requestId=" + requestId);
        Optional<RequestData> optionalRequestData = requestRepository.findByRequestId(requestId);

        if (optionalRequestData.isPresent())
        {
            LOGGER.error("Request with that id already exists");
            throw new IllegalArgumentException("Request with that id already exists");
        }
    }
}
