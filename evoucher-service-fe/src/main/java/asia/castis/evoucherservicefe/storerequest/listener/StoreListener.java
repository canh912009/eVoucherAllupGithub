package asia.castis.evoucherservicefe.storerequest.listener;

import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.exceptions.InvalidException;
import asia.castis.evoucherservicefe.storerequest.dto.StoreRequest;
import asia.castis.evoucherservicefe.storerequest.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class StoreListener {
    private StoreService storeService;

    @Autowired
    public StoreListener(StoreService storeService) {
        this.storeService = storeService;
    }

    @RabbitListener(queues = "${rabbitmq.in.queue.store}")
    public void receivedMessage(@Payload Message<List<StoreRequest>> message) throws InvalidException, IOException {
        log.info("=============== Received Publish From RabbitMQ ===============");
        log.info("JSON={}", JsonMapper.safeWriteValueAsString(message.getPayload()));
        storeService.syncStore(message.getPayload());
    }
}
