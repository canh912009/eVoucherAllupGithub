package asia.castis.evoucherservicefe.publishrequest.listener;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.exceptions.*;
import asia.castis.evoucherservicefe.publishrequest.service.PublishRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishRequestListener {
    private final PublishRequestService publishRequestService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${rabbitmq.in.queue.publish}"),
                    exchange = @Exchange(value = "${rabbitmq.in.exchange.publish}", durable = "${rabbitmq.in.durable.publish}"),
                    key = "${rabbitmq.in.routingKey.publish}"
            )
    )
    public void receivedMessage(@Payload Message<RequestFromBE> message)
            throws CreateMessageException, InvalidException, NotFoundException, IOException, RetryJobException, DecryptException, SendMessageToQueueException {
        log.info("=============== Received Publish From RabbitMQ ===============");
        log.info("JSON={}", JsonMapper.safeWriteValueAsString(message.getPayload()));
        publishRequestService.incomingPublishHandling(message.getPayload(), new Date());
    }
}
