package asia.castis.evoucherservicefe.disablevoucher.sennder;

import asia.castis.evoucherservicefe.common.utils.QueueSenderUtils;
import asia.castis.evoucherservicefe.disablevoucher.dto.VoucherDisableProcessResponse;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DisableResultSender {

    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.out.exchange.disableResult}")
    private String disableVoucherExchange;
    @Value("${rabbitmq.out.routingKey.disableResult}")
    private String disableVoucherRoutingKey;
    @Autowired
    public DisableResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDisableVoucherResult(VoucherDisableProcessResponse msg) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, disableVoucherExchange, disableVoucherRoutingKey, msg);
    }

}