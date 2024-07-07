package com.egtdigitaltask.gateway.model.dto;

import java.util.Map;

public class CurrentExchangeRateResponse extends ExchangeRateResponse
{
    private long timestamp;
    private Map<String, Double> exchangeRates;

    public CurrentExchangeRateResponse(String base, long timestamp, Map<String, Double> exchangeRates)
    {
        super(base);
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
    }

    public CurrentExchangeRateResponse() {}

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
