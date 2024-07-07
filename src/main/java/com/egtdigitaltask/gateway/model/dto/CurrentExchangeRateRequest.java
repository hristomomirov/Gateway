package com.egtdigitaltask.gateway.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CurrentExchangeRateRequest
{
    @NotBlank(message = "Request id must not be null or empty")
    private String requestId;

    @Positive(message = "Timestamp must be a positive number")
    private long timestamp;

    @NotBlank(message = "Client must not be null or empty")
    private String client;

    @NotBlank(message = "Currency must not be null or empty")
    @Size(min = 3, max = 3, message = "Currency ticker not valid")
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
