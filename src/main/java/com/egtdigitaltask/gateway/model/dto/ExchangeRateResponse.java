package com.egtdigitaltask.gateway.model.dto;

public abstract class ExchangeRateResponse
{
    private String base;

    public ExchangeRateResponse() {}

    public ExchangeRateResponse(String base)
    {
        this.base = base;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }
}
