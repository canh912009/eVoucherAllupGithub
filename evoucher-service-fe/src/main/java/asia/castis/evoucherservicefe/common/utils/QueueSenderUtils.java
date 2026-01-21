package asia.castis.evoucherservicefe.common.utils;

import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class QueueSenderUtils {
    public static void sendToRabbitMQ(RabbitTemplate rabbitTemplate, String exchangeName, String routingKey, QueueMessage queueMessage)
            throws SendMessageToQueueException {
        try {
            // check if exchange exists, if not create new one
            rabbitTemplate.convertAndSend(exchangeName, routingKey, queueMessage);
            log.info("=============== Sent to exchange name={}, routingKey={} ===============", exchangeName, routingKey);
            log.info("JSON={}", JsonMapper.safeWriteValueAsString(queueMessage));
        } catch (AmqpException e) {
            throw new SendMessageToQueueException(String.format("AMQP exception while sending to queue, msg=%s", e.getMessage()), e);
        } catch (RuntimeException e) {
            throw new SendMessageToQueueException(String.format("Exception while sending to queue, msg=%s", e.getMessage()), e);
        }
    }
}
