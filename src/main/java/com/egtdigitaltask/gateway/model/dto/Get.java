package com.egtdigitaltask.gateway.model.dto;

import jakarta.validation.constraints.NotBlank;

public class Get
{
    @NotBlank(message = "Consumer must not be null or empty")
    private String consumer;

    @NotBlank(message = "Currency must not be null or empty")
    private String currency;

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
}
