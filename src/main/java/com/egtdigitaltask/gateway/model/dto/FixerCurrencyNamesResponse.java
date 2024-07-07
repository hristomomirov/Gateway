package com.egtdigitaltask.gateway.model.dto;

import java.util.Map;

public class FixerCurrencyNamesResponse
{
    private boolean success;

    private Map<String, String> symbols;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public Map<String, String> getSymbols()
    {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols)
    {
        this.symbols = symbols;
    }
}
