package com.egtdigitaltask.gateway.utill;

import com.egtdigitaltask.gateway.model.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Validator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static void validateUniqueRequestId(Optional<RequestData> requestData)
    {
        if (requestData.isPresent())
        {
            LOGGER.error("Request with that id already exists");
            throw new IllegalArgumentException("Request with that id already exists");
        }
    }
}
