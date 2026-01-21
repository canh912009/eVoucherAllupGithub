package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RabbitMQProducerService {
    @Value("${queue.out.exchange}")
    private String exchange;
    @Value("${queue.out.routingKey}")
    private String routingKey;
    @Value("${queue.out.name}")
    private String outQueue;
    @Value("${queue.in.exchange}")
    private String inExchange;
    @Value("${queue.in.routingKey}")
    private String inRoutingKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducerService(RabbitTemplate rabbitTemplate) {
       this.rabbitTemplate = rabbitTemplate;
    }
    public void sendingMessageInquiry(SendingVoucherMessageInquiry sendingVoucherMessageInquiry) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, sendingVoucherMessageInquiry);
        } catch (AmqpException e) {
            log.info("AmqpException when sending inquiry for publishScheduleId {}", sendingVoucherMessageInquiry.getPublishScheduleId());
        }
    }
    public void sendingMessageInquiryList(List<SendingVoucherMessageInquiry> sendingVoucherMessageInquiryList) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, sendingVoucherMessageInquiryList);
        } catch (AmqpException e) {
            log.info("AmqpException when sending inquiry for sending message inquiry");
        }
    }
    public void sendingPublish(SendingPublish sendingPublish) {
        log.info("sendingPublish");
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, sendingPublish);
        } catch (AmqpException e) {
            log.info("AmqpException when sending inquiry for sending message inquiry");
            log.info(e.getMessage());
        }
    }
    public void sendingPublishList(List<SendingPublish> sendingPublishList) {
        log.info("sendingPublishList");
        try {
            //rabbitTemplate.convertAndSend(exchange, routingKey, sendingPublishList);
            rabbitTemplate.convertAndSend(exchange, routingKey, sendingPublishList);
        } catch (AmqpException e) {
            log.info("AmqpException when sending inquiry for sending message inquiry");
            log.info(e.getMessage());
        }
    }
    public void sendPublishVoucherMessage(PublishVoucherMessage publishVoucherMessage) {
       rabbitTemplate.convertAndSend(inExchange, inRoutingKey, publishVoucherMessage);
    }
    public void sendIncomPublish(IncomingPublish incomingPublish) {
        log.info("sendIncomPublish {}", incomingPublish.getPublishId());
        try {
            log.info("try convertAndSend incomingPublish with publishId: {}", incomingPublish.getPublishId());
            rabbitTemplate.convertAndSend(inExchange, inRoutingKey, incomingPublish);
        } catch (AmqpException e) {
            log.info("AmqpException when sending incom publish");
            log.info(e.getMessage());
        }
    }

    public void sendInvalidIncomPublish() {
        try {
            rabbitTemplate.convertAndSend(inExchange, inRoutingKey, "Invalid IncomPublish");
        } catch (AmqpException e) {
            log.info("AmqpException when sending invalid incom publish");
            log.info(e.getMessage());
        }
    }
}
