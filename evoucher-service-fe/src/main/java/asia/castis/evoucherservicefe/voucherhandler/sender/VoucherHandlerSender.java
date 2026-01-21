package asia.castis.evoucherservicefe.voucherhandler.sender;

import asia.castis.evoucherservicefe.common.utils.QueueSenderUtils;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VoucherHandlerSender {

    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.out.exchange.activate}")
    private String activateExchange;
    @Value("${rabbitmq.out.routingKey.activate}")
    private String activateRoutingKey;
    @Autowired
    public VoucherHandlerSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendActivateRequest(ActivateRequest messageResult) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, activateExchange, activateRoutingKey, messageResult);
    }

}