package com.egtdigitaltask.gateway.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class History
{
    @NotBlank(message = "Consumer must not be null or empty")
    private String consumer;

    @NotBlank(message = "Currency must not be null or empty")
    private String currency;

    @Positive(message = "Period must be a positive number")
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
