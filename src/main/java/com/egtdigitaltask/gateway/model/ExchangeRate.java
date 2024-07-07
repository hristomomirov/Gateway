package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "base_ticker", referencedColumnName = "ticker")
    private Currency base;

    @ManyToOne
    @JoinColumn(name = "target_ticker", referencedColumnName = "ticker")
    private Currency target;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "rate")
    private double rate;

    public ExchangeRate()
    {
    }

    public ExchangeRate(Currency base, Currency target, long timestamp, double rate)
    {
        this.base = base;
        this.target = target;
        this.timestamp = timestamp;
        this.rate = rate;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Currency getBase()
    {
        return base;
    }

    public void setBase(Currency base)
    {
        this.base = base;
    }

    public Currency getTarget()
    {
        return target;
    }

    public void setTarget(Currency target)
    {
        this.target = target;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }

}
