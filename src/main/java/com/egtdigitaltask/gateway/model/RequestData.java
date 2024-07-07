package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "requests",
       uniqueConstraints = @UniqueConstraint(columnNames = "request_id"))
public class RequestData implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "time")
    private long timestamp;

    @Column(name = "end_client_id")
    private String endClientId;

    public RequestData()
    {
    }

    public RequestData(String serviceName, String requestId, long time, String endClientId)
    {
        this.serviceName = serviceName;
        this.requestId = requestId;
        this.timestamp = time;
        this.endClientId = endClientId;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
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

    public String getEndClientId()
    {
        return endClientId;
    }

    public void setEndClientId(String endClientId)
    {
        this.endClientId = endClientId;
    }
}
