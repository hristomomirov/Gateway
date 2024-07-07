package com.egtdigitaltask.gateway.model.dto;


public class CommandRequest
{
    private String id;

    private Get get;

    private History history;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Get getGet()
    {
        return get;
    }

    public void setGet(Get get)
    {
        this.get = get;
    }

    public History getHistory()
    {
        return history;
    }

    public void setHistory(History history)
    {
        this.history = history;
    }
}

