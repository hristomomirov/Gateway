package com.egtdigitaltask.gateway.model.dto;

import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateHistory history = (ExchangeRateHistory) o;
        return timestamp == history.timestamp && Objects.equals(exchangeRates, history.exchangeRates);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(timestamp, exchangeRates);
    }
}
