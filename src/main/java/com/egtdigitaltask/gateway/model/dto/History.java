package com.egtdigitaltask.gateway.model.dto;

public class History
{
    private String consumer;
    private String currency;
    private long period;

    public String getConsumer()
    {
        return consumer;
    }

    public void setConsumer(String consumer)
    {
        this.consumer = consumer;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public long getPeriod()
    {
        return period;
    }

    public void setPeriod(long period)
    {
        this.period = period;
    }
}
