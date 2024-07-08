package com.egtdigitaltask.gateway.model.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class CommandRequest
{
    @NotBlank(message = "Id must not be null or empty")
    private String id;

    @Valid
    private Get get;

    @Valid
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

