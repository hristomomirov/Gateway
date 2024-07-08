package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exchange_rates",
       uniqueConstraints = @UniqueConstraint(name = "exchange_rates_uc_base_ticker_target_ticker_timestamp",
                                             columnNames = {"base_ticker", "target_ticker", "timestamp"}))
public class ExchangeRate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "base_ticker", referencedColumnName = "ticker", nullable = false)
    private Currency base;

    @ManyToOne
    @JoinColumn(name = "target_ticker", referencedColumnName = "ticker", nullable = false)
    private Currency target;

    @Column(name = "timestamp", nullable = false)
    private long timestamp;

    @Column(name = "rate", nullable = false)
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
