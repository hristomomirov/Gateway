package com.egtdigitaltask.gateway.model.dto;

public class CurrentExchangeRateRequest
{
    private String requestId;
    private long timestamp;
    private String client;
    private String currency;

    public CurrentExchangeRateRequest() {}

    public CurrentExchangeRateRequest(String requestId, long timestamp, String client, String currency)
    {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.client = client;
        this.currency = currency;
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

    @Override
    public String toString()
    {
        return "RequestCurrentExchangeRateDto{" +
               "requestId='" + requestId + '\'' +
               ", timestamp=" + timestamp +
               ", client='" + client + '\'' +
               ", currency='" + currency + '\'' +
               '}';
    }
}
