package asia.castis.evoucher.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;
    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${rabbitmq.queue.USED_VOUCHER}")
    private String useVoucherQueue;
    @Value("${rabbitmq.routingKey.USED_VOUCHER}")
    private String useVoucherRoutingKey;
    @Value("${rabbitmq.exchange.USED_VOUCHER}")
    private String useVoucherExchange;

    @Value("${rabbitmq.queue.TRANSFER_VOUCHER}")
    private String transferVoucherQueue;
    @Value("${rabbitmq.routingKey.TRANSFER_VOUCHER}")
    private String transferVoucherRoutingKey;
    @Value("${rabbitmq.exchange.TRANSFER_VOUCHER}")
    private String transferVoucherExchange;

    @Value("${rabbitmq.queue.RECEIPT_VOUCHER}")
    private String receiptVoucherQueue;
    @Value("${rabbitmq.routingKey.RECEIPT_VOUCHER}")
    private String receiptVoucherRoutingKey;
    @Value("${rabbitmq.exchange.RECEIPT_VOUCHER}")
    private String receiptVoucherExchange;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitHost);
        connectionFactory.setPort(rabbitPort);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        // Set any additional properties as needed
        return connectionFactory;
    }

    @Bean
    public Queue queueUsedVoucher() {
        return new Queue(useVoucherQueue);
    }

    @Bean
    public DirectExchange outUsedVoucher() {
        return new DirectExchange(useVoucherExchange);
    }

    @Bean
    public Binding bindingUsedVoucher() {
        return BindingBuilder.
                bind(queueUsedVoucher())
                .to(outUsedVoucher())
                .with(useVoucherRoutingKey);
    }


    @Bean
    public Queue queueTransferVoucher() {
        return new Queue(transferVoucherQueue);
    }

    @Bean
    public DirectExchange outTransferVoucher() {
        return new DirectExchange(transferVoucherExchange);
    }

    @Bean
    public Binding bindingTransferVoucher() {
        return BindingBuilder.
                bind(queueTransferVoucher())
                .to(outTransferVoucher())
                .with(transferVoucherRoutingKey);
    }

    @Bean
    public Queue queueReceiptVoucher() {
        return new Queue(receiptVoucherQueue);
    }

    @Bean
    public DirectExchange outReceiptVoucher() {
        return new DirectExchange(receiptVoucherExchange);
    }

    @Bean
    public Binding bindingReceiptVoucher() {
        return BindingBuilder.
                bind(queueReceiptVoucher())
                .to(outReceiptVoucher())
                .with(receiptVoucherRoutingKey);
    }
}
