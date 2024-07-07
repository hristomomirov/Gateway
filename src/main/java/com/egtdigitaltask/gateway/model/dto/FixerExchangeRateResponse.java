package com.egtdigitaltask.gateway.model.dto;

import java.time.LocalDate;
import java.util.Map;

public class FixerExchangeRateResponse
{
    private boolean success;

    private long timestamp;

    private String base;

    private LocalDate date;

    private Map<String, Double> rates;

    public FixerExchangeRateResponse()
    {
    }

    public FixerExchangeRateResponse(boolean success, long timestamp, String base, LocalDate date, Map<String, Double> rates)
    {
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Map<String, Double> getRates()
    {
        return rates;
    }

    public void setRates(Map<String, Double> rates)
    {
        this.rates = rates;
    }

    @Override
    public String toString()
    {
        return "FixerExchangeRate{" +
               "success=" + success +
               ", timestamp=" + timestamp +
               ", base='" + base + '\'' +
               ", date=" + date +
               ", rates=" + rates +
               '}';
    }
}
