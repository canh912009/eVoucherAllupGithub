package com.castis.publishservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.ErrorHandler;

/**
 * RabbitMQConfig
 *
 * @author by daont on 08/05/2023
 * @project publish-service
 */


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.create-message}")
    private String queue;
    @Value("${rabbitmq.queue.create-message-result}")
    private String createMessageResult;
    @Value("${rabbitmq.queue.send-message-result}")
    private String sendMessageResult;
    @Value("${rabbitmq.queue.voucher-handover}")
    private String voucherHandoverQueueName;
    @Value("${rabbitmq.queue.update-store}")
    private String updateStoreQueueName;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.create-message}")
    private String routingKey;
    @Value("${rabbitmq.routing-key.send-message-result}")
    private String sendMessageResultKey;
    @Value("${rabbitmq.routing-key.create-message-result}")
    private String createMessageResultKey;
    @Value("${rabbitmq.routing-key.voucher-handover}")
    private String handoverRoutingKey;
    @Value("${rabbitmq.routing-key.update-store}")
    private String updateStoreRoutingKey;

    @Value("${rabbitmq.queue.resend-voucher}")
    private String queueResendVoucher;
    @Value("${rabbitmq.routing-key.resend-voucher}")
    private String routingKeyResendVoucher;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    //	@Bean
//	public ConnectionNameStrategy cns() {
//		return new SimplePropertyValueConnectionNameStrategy("spring.rabbitmq");
//	}
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        //		jsonConverter.get
//		jsonConverter.setClassMapper(classMapper());
        return new Jackson2JsonMessageConverter();
    }
//	@Bean
//	public DefaultClassMapper classMapper() {
//		DefaultClassMapper classMapper = new DefaultClassMapper();
//		Map<String, Class<?>> idClassMapping = new HashMap<>();
//		idClassMapping.put("statusQueue", StatusQueue.class);
//		classMapper.setIdClassMapping(idClassMapping);
//		return classMapper;
//	}


    // spring bean for rabbitmq queue
    @Bean
    public Queue vouhcerQueue() {
        return new Queue(queue);
    }

    @Bean
    Queue createMessageResult() {
        return new Queue(createMessageResult);
    }

    @Bean
    Queue sendMessageResultQueue() {
        return new Queue(sendMessageResult);
    }

    @Bean
    Queue handOverRequestQueue() {
        return new Queue(voucherHandoverQueueName);
    }
    @Bean
    Queue updateStoreQueue() {
        return new Queue(updateStoreQueueName);
    }

    // spring bean for rabbitmq exchange
    @Bean(name = "directExchange")
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // binding between queue and exchange using routing key
    @Bean
    public Binding binding(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(sendMessageResultQueue())
                .to(directExchange)
                .with(sendMessageResultKey);

    }

    @Bean
    public Binding bindingVoucherQueue(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(vouhcerQueue())
                .to(directExchange)
                .with(routingKey);

    }

    @Bean
    public Binding bindingVoucherHandOver(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(handOverRequestQueue())
                .to(directExchange)
                .with(handoverRoutingKey)
                ;

    }

    @Bean
    public Binding bindingCreateMessageResultQueue(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(createMessageResult())
                .to(directExchange)
                .with(createMessageResultKey);

    }
    @Bean
    public Binding bindingUpdateStoreQueue(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(updateStoreQueue())
                .to(directExchange)
                .with(updateStoreRoutingKey);

    }

    @Bean
    public Queue resendVoucherQueue() {
        return new Queue(queueResendVoucher);
    }

    @Bean
    public Binding bindingResendVoucher(
            @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder
                .bind(resendVoucherQueue())
                .to(directExchange)
                .with(routingKeyResendVoucher);
    }



//    @Bean
//    public SimpleRabbitListenerContainerFactory customListenerContainerFactory(ConnectionFactory connectionFactory,
//                                                                               MessageConverter jsonMessageConverter) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter);
//        return factory;
//    }


    @Bean("rabbitListenerContainerFactory")
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory
            (ConnectionFactory connectionFactory,
             MessageConverter jsonMessageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }
    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new CustomExceptionStrategy();
    }

    // Spring boot autoconfiguration provides following beans
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin
}