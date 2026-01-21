package asia.castis.evoucherservicefe.job.sender;

import asia.castis.evoucherservicefe.common.utils.QueueSenderUtils;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import asia.castis.evoucherservicefe.job.dto.VoucherJobResultDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JobResultSender {

    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.out.exchange.jobResult}")
    private String resultExchange;
    @Value("${rabbitmq.out.routingKey.jobResult}")
    private String resultRoutingKey;

    @Autowired
    public JobResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJobResult(VoucherJobResultDto messageResult) throws SendMessageToQueueException {
        QueueSenderUtils.sendToRabbitMQ(rabbitTemplate, resultExchange, resultRoutingKey, messageResult);
    }

}