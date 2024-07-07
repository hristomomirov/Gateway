package com.egtdigitaltask.gateway.model.dto;

import java.util.Map;

public class ExchangeRateHistory
{
    private long timestamp;

    private Map<String, Double> exchangeRates;

    public ExchangeRateHistory() {}

    public ExchangeRateHistory(long timestamp, Map<String, Double> exchangeRates)
    {
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Map<String, Double> getExchangeRates()
    {
        return exchangeRates;
    }

    public void setExchangeRates(Map<String, Double> exchangeRates)
    {
        this.exchangeRates = exchangeRates;
    }
}
