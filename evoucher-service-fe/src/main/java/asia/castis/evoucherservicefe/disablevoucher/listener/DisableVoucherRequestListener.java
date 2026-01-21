package asia.castis.evoucherservicefe.disablevoucher.listener;

import asia.castis.evoucherservicefe.common.utils.LocalDateUtils;
import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.disablevoucher.dto.VoucherDisableProcessRequest;
import asia.castis.evoucherservicefe.disablevoucher.service.DisableVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DisableVoucherRequestListener {
    private DisableVoucherService disableVoucherService;

    @Autowired
    public DisableVoucherRequestListener(DisableVoucherService disableVoucherService) {
        this.disableVoucherService = disableVoucherService;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${rabbitmq.in.queue.disable}"),
                    exchange = @Exchange(value = "${rabbitmq.in.exchange.disable}", durable = "${rabbitmq.in.durable.disable}"),
                    key = "${rabbitmq.in.routingKey.disable}"
            )
    )
    public void receivedMessage(@Payload Message<VoucherDisableProcessRequest> message) {
        log.info("=============== Received Disable request From RabbitMQ ===============");
        log.info("JSON={}", JsonMapper.safeWriteValueAsString(message.getPayload()));
        disableVoucherService.disableVoucher(message.getPayload(), LocalDateUtils.getCurrentDate());
    }
}
