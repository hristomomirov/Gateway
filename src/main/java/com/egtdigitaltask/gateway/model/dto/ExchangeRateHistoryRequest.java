package com.egtdigitaltask.gateway.model.dto;

public class ExchangeRateHistoryRequest
{
    private String requestId;
    private long timestamp;
    private String client;
    private String currency;
    private long period;

    public ExchangeRateHistoryRequest() {}

    public ExchangeRateHistoryRequest(String requestId, long timestamp, String client, String currency,
                                      long period)
    {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.client = client;
        this.currency = currency;
        this.period = period;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
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

    @Override
    public String toString()
    {
        return "RequestExchangeRateHistoryDto{" +
               "requestId='" + requestId + '\'' +
               ", timestamp=" + timestamp +
               ", client='" + client + '\'' +
               ", currency='" + currency + '\'' +
               ", period=" + period +
               '}';
    }
}
