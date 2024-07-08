package com.egtdigitaltask.gateway.model.dto;

import java.util.List;

public class ExchangeRateHistoryResponse extends ExchangeRateResponse
{
    private List<ExchangeRateHistory> histories;

    public ExchangeRateHistoryResponse() {}

    public ExchangeRateHistoryResponse(String base, List<ExchangeRateHistory> history)
    {
        super(base);
        this.histories = history;
    }

    public List<ExchangeRateHistory> getHistories()
    {
        return histories;
    }

    public void setHistories(List<ExchangeRateHistory> histories)
    {
        this.histories = histories;
    }
}
