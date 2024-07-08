package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "requests",
       uniqueConstraints = @UniqueConstraint(name = "requests_request_id",
                                             columnNames = "request_id"))
public class RequestData implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "time", nullable = false)
    private long timestamp;

    @Column(name = "end_client_id", nullable = false)
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


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestData that = (RequestData) o;
        return id == that.id && timestamp == that.timestamp && Objects.equals(serviceName, that.serviceName)
               && Objects.equals(requestId, that.requestId) && Objects.equals(endClientId,
                                                                              that.endClientId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, serviceName, requestId, timestamp, endClientId);
    }
}
