package com.evoucher.evoucherbe.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfiguration {
    @Value("${queues.exchange}")
    private String commonExchange;

    @Value("${queues.used_voucher.queue}")
    private String queueUseVoucher;
    @Value("${queues.used_voucher.routingKey}")
    private String routingKeyUseVoucher;

    @Value("${queues.receive_voucher.queue}")
    private String queueReceiveVoucher;
    @Value("${queues.receive_voucher.routingKey}")
    private String routingKeyReceiveVoucher;
    @Value("${queues.activate_voucher.queue}")
    private String queueActivateVoucher;
    @Value("${queues.activate_voucher.routingKey}")
    private String routingKeyActivateVoucher;

    @Value("${queues.update_voucher.queue}")
    private String queueUpdateVoucher;
    @Value("${queues.update_voucher.routingKey}")
    private String routingKeyUpdateVoucher;

    @Value("${queues.disable_voucher.queue}")
    private String queueDisableVoucher;
    @Value("${queues.disable_voucher.routingKey}")
    private String routingKeyDisableVoucher;

    @Value("${queues.disable_voucher_result.queue}")
    private String queueDisableVoucherResult;
    @Value("${queues.disable_voucher_result.routingKey}")
    private String routingKeyDisableVoucherResult;


    @Bean(name = "directExchange")
    public DirectExchange exchange() {
        return new DirectExchange(commonExchange);
    }

    @Bean
    public Queue usedVoucherQueue() {
        return new Queue(queueUseVoucher);
    }


    @Bean
    public Binding bindingUsedVoucher(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(usedVoucherQueue())
                .to(directExchange)
                .with(routingKeyUseVoucher);

    }

    @Bean
    public Queue receiveVoucherQueue() {
        return new Queue(queueReceiveVoucher);
    }


    @Bean
    public Binding bindingReceiveVoucher(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(receiveVoucherQueue())
                .to(directExchange)
                .with(routingKeyReceiveVoucher);

    }
    @Bean
    public Queue activateVoucherQueue() {
        return new Queue(queueActivateVoucher);
    }


    @Bean
    public Binding bindingActivateVoucher(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(activateVoucherQueue())
                .to(directExchange)
                .with(routingKeyActivateVoucher);
    }

    @Bean
    public Queue updateVoucherQueue() {
        return new Queue(queueUpdateVoucher);
    }


    @Bean
    public Binding bindingUpdateVoucher(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(updateVoucherQueue())
                .to(directExchange)
                .with(routingKeyUpdateVoucher);

    }

    @Bean
    public Queue disableVoucherQueue() {
        return new Queue(queueDisableVoucher);
    }


    @Bean
    public Binding bindingDisableVoucher(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(disableVoucherQueue())
                .to(directExchange)
                .with(routingKeyDisableVoucher);
    }

    @Bean
    public Queue disableVoucherResultQueue() {
        return new Queue(queueDisableVoucherResult);
    }

    @Bean
    public Binding bindingDisableVoucherResult(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(disableVoucherResultQueue())
                .to(directExchange)
                .with(routingKeyDisableVoucherResult);
    }
}
