package com.castis.publishservice.producer;

import com.castis.publishservice.dto.queue.PublishQueueRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RabbitMQProducer
 *
 * @author by daont on 08/05/2023
 * @project publish-service
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQProducer {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.create-message}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishEvoucher(PublishQueueRequest message) {
        log.info("Queue {} sent\n{}", routingKey, gson.toJson(message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        log.info("Publish voucher successfully");
        log.info("=================================================");
    }
}
