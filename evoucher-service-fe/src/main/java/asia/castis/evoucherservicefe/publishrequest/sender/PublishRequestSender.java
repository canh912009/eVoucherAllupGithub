package asia.castis.evoucherservicefe.publishrequest.sender;

import asia.castis.evoucherservicefe.common.utils.QueueSenderUtils;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.common.dto.createmessageresult.OutgoingPublishResult;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.RequestToPushAgent;
import asia.castis.evoucherservicefe.publishrequest.dto.OtpSmsQueueMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublishRequestSender {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.out.exchange.publish}")
    private String sendVoucherExchange;
    @Value("${rabbitmq.out.routingKey.publish}")
    private String sendVoucherRoutingKey;
    @Value("${rabbitmq.out.exchange.result}")
    private String resultExchange;
    @Value("${rabbitmq.out.routingKey.result}")
    private String resultRoutingKey;

    @Value("${rabbitmq.out.exchange.otp}")
    private String otpExchange;
    @Value("${rabbitmq.out.routingKey.otp}")
    private String otpRoutingKey;
    @Autowired
    public PublishRequestSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessageResult(OutgoingPublishResult messageResult) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, resultExchange, resultRoutingKey, messageResult);
    }

    public void sendRequestToPushAgent(RequestToPushAgent publish) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, sendVoucherExchange, sendVoucherRoutingKey, publish);
    }
    public void sendOtpSMS(OtpSmsQueueMsg msg) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, otpExchange, otpRoutingKey, msg);
    }

}