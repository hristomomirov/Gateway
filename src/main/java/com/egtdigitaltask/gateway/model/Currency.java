package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency
{
    @Id
    @Column(name = "ticker", nullable = false, length = 3)
    private String ticker;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    public Currency() {}

    public Currency(String ticker, String fullName)
    {
        this.ticker = ticker;
        this.fullName = fullName;
    }

    public String getTicker()
    {
        return ticker;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
}
