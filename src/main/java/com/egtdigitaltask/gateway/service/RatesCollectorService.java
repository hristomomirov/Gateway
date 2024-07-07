package com.egtdigitaltask.gateway.service;

import com.egtdigitaltask.gateway.model.Currency;
import com.egtdigitaltask.gateway.model.ExchangeRate;
import com.egtdigitaltask.gateway.model.dto.FixerCurrencyNamesResponse;
import com.egtdigitaltask.gateway.model.dto.FixerExchangeRateResponse;
import com.egtdigitaltask.gateway.repository.CurrenciesRepository;
import com.egtdigitaltask.gateway.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatesCollectorService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesCollectorService.class);
    private static final String DEFAULT_BASE_CURRENCY = "EUR";
    private final FixerWebClient webClient;
    private final CurrenciesRepository currenciesRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public RatesCollectorService(FixerWebClient webClient,
                                 CurrenciesRepository currenciesRepository,
                                 ExchangeRateRepository exchangeRateRepository)
    {
        this.webClient = webClient;
        this.currenciesRepository = currenciesRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @PostConstruct
    public void insertAllAvailableCurrencies()
    {
        FixerCurrencyNamesResponse response = webClient.getCurrencyNames();

        if (!response.isSuccess())
        {
            LOGGER.warn("Could not fetch currencies from Fixer.io");
        }
        else if (response.getSymbols().isEmpty())
        {
            LOGGER.warn("Received empty list of currencies from Fixer.io");
        }
        else
        {
            // because of unique constraint on ticker duplicates will just update their full_name
            LOGGER.debug("Inserting currencies in database");
            response.getSymbols().forEach(currenciesRepository::upsert);
        }
    }

    // due to subscription limitations only EUR can be used as base currency
    @Scheduled(initialDelay = 0, fixedRateString = "${scheduling.fixedRate}")
    @Transactional
    public void insertExchangeRates()
    {
        LOGGER.warn("Due to subscription limitations only EUR can be used as base currency");

        LOGGER.debug("Fetching currencies from database");
        List<Currency> currencies = currenciesRepository.findAll();

        if (currencies.isEmpty())
        {
            LOGGER.warn("Could not retrieve list of currencies from database");
            return;
        }

        String comaSeparatedCurrencyTickers = currencies.stream()
                                                        .map(Currency::getTicker)
                                                        .filter(s -> !s.equals(DEFAULT_BASE_CURRENCY))
                                                        .collect(Collectors.joining(","));

        // get all exchange rates for eur from fixer
        LOGGER.info("Getting exchange rates from Fixer.io with base currency=" + DEFAULT_BASE_CURRENCY);
        FixerExchangeRateResponse response = webClient.getFixerExchangeRate(DEFAULT_BASE_CURRENCY,
                                                                            comaSeparatedCurrencyTickers);
        if (!response.isSuccess())
        {
            LOGGER.warn("Could not fetch exchange rates from Fixer.io");
            return;
        }

        List<ExchangeRate> rates = extractExchangeRates(currencies, response);

        if (rates.isEmpty())
        {
            LOGGER.warn("No currencies have been received from Fixer.io, exchange rates will not be inserted");
            return;
        }

        // insert into db
        LOGGER.debug("Inserting exchange rates in database");
        exchangeRateRepository.saveAll(rates);
    }

    private static List<ExchangeRate> extractExchangeRates(List<Currency> currencies,
                                                           FixerExchangeRateResponse response)
    {
        Optional<Currency> optionalBaseCurrency = getCurrencyByTicker(currencies, DEFAULT_BASE_CURRENCY);
        if (optionalBaseCurrency.isEmpty())
        {
            return List.of();
        }

        Currency base = optionalBaseCurrency.get();
        Map<String, Currency> currenciesMap = currencies.stream()
                                                        .collect(Collectors.toMap(Currency::getTicker,
                                                                                  e -> e,
                                                                                  (a, b) -> a));
        return response.getRates().entrySet().stream()
                       .map(entry -> new ExchangeRate(base, currenciesMap.get(entry.getKey()),
                                                      response.getTimestamp(),
                                                      entry.getValue()))
                       .toList();
    }

    private static Optional<Currency> getCurrencyByTicker(List<Currency> currencies, String ticker)
    {
        return currencies.stream().filter(c -> c.getTicker().equals(ticker)).findFirst();
    }
}
