package com.egtdigitaltask.gateway.model.dto;

import java.util.List;

public class ExchangeRateHistoryResponse extends ExchangeRateResponse
{
    private List<ExchangeRateHistory> history;

    public ExchangeRateHistoryResponse() {}

    public ExchangeRateHistoryResponse(String base, List<ExchangeRateHistory> history)
    {
        super(base);
        this.history = history;
    }

    public List<ExchangeRateHistory> getHistory()
    {
        return history;
    }

    public void setHistory(List<ExchangeRateHistory> history)
    {
        this.history = history;
    }
}
