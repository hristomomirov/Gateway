package com.egtdigitaltask.gateway.service;

import com.egtdigitaltask.gateway.model.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer
{
    @Value("${constants.rabbitmq.exchange}")
    private String exchange;

    @Value("${constants.rabbitmq.routingKey}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducer.class);

    private final RabbitTemplate template;

    @Autowired
    public RabbitMqProducer(RabbitTemplate template)
    {
        this.template = template;
    }

    public void sendMessage(RequestData requestData)
    {
        LOGGER.info("Sending message to queue");
        template.convertAndSend(exchange, routingKey, requestData);
    }
}
