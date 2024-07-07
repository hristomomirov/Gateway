package com.egtdigitaltask.gateway.model;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency
{
    @Column(name = "ticker")
    @Id
    private String ticker;

    @Column(name = "full_name")
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
