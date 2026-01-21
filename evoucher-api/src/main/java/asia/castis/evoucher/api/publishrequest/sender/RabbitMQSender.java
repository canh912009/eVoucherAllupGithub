package asia.castis.evoucher.api.publishrequest.sender;

import asia.castis.evoucher.api.elastic.model.publish.RabbitPaymentHistory;
import asia.castis.evoucher.api.publishrequest.PublishReceiptVoucher;
import asia.castis.evoucher.api.publishrequest.PublishTransferVoucher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Value("${rabbitmq.routingKey.USED_VOUCHER}")
    private String useVoucherRouting;
    @Value("${rabbitmq.exchange.USED_VOUCHER}")
    private String useVoucherExchange;

    @Value("${rabbitmq.routingKey.TRANSFER_VOUCHER}")
    private String transferVoucherRouting;
    @Value("${rabbitmq.exchange.TRANSFER_VOUCHER}")
    private String transferVoucherExchange;

    @Value("${rabbitmq.routingKey.RECEIPT_VOUCHER}")
    private String receiptVoucherRouting;
    @Value("${rabbitmq.exchange.RECEIPT_VOUCHER}")
    private String receiptVoucherExchange;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    public void sendPaymentHistory(RabbitPaymentHistory object) {
        try {
            rabbitTemplate.convertAndSend(useVoucherExchange, useVoucherRouting, object);
            logger.info("Sent to exchange [name:{}, routingKey:{}, body:{}]", "", useVoucherRouting, object);
            System.out.println("Send msg = " + object);
        } catch (Exception e) {
            logger.info("sendPaymentHistory Error [e:{}, routingKey:{}]", e, object);
            e.printStackTrace();
        }
    }

    public void sendTransferHistory(PublishTransferVoucher object) {
        try {
            rabbitTemplate.convertAndSend(transferVoucherExchange, transferVoucherRouting, object);
            logger.info("Sent to exchange [name:{}, routingKey:{}, body:{}]", transferVoucherExchange, transferVoucherRouting, object);
        } catch (Exception e) {
            logger.info("sendTransferHistory Error [e:{}, routingKey:{}]", e, object);
            e.printStackTrace();
        }
    }

    public void sendReceiptHistory(PublishReceiptVoucher object) {
        try {
            rabbitTemplate.convertAndSend(receiptVoucherExchange, receiptVoucherRouting, object);
            logger.info("Sent to exchange [name:{}, routingKey:{}, body:{}]", receiptVoucherExchange, receiptVoucherRouting, object);
        } catch (Exception e) {
            logger.info("sendTransferHistory Error [e:{}, routingKey:{}]", e, object);
            e.printStackTrace();
        }
    }
}
