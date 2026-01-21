package asia.castis.evoucherservicefe.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQueueConfig {
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;
    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;
    @Value("${rabbitmq.in.exchange.store}")
    private String storeExchange;
    @Value("${rabbitmq.in.queue.store}")
    private String storeQueue;
    @Value("${rabbitmq.in.routingKey.store}")
    private String storeRoutingKey;
    @Value("${rabbitmq.in.durable.store}")
    private boolean storeDurable;
    @Value("${rabbitmq.in.exchange.publish}")
    private String inPublishExchange;
    @Value("${rabbitmq.in.queue.publish}")
    private String inPublishQueue;
    @Value("${rabbitmq.in.routingKey.publish}")
    private String inPublishRoutingKey;
    @Value("${rabbitmq.in.durable.publish}")
    private boolean inPublishDurable;
    @Value("${rabbitmq.out.exchange.publish}")
    private String outPublishExchange;
    @Value("${rabbitmq.out.queue.publish}")
    private String outPublishQueue;
    @Value("${rabbitmq.out.routingKey.publish}")
    private String outPublishRoutingKey;
    @Value("${rabbitmq.out.durable.publish}")
    private boolean outPublishDurable;
    @Value("${rabbitmq.out.exchange.result}")
    private String outResultExchange;
    @Value("${rabbitmq.out.queue.result}")
    private String outResultQueue;
    @Value("${rabbitmq.out.routingKey.result}")
    private String outResultRoutingKey;
    @Value("${rabbitmq.out.durable.result}")
    private boolean outResultDurable;
    @Value("${rabbitmq.out.exchange.jobResult}")
    private String outJobResultExchange;
    @Value("${rabbitmq.out.queue.jobResult}")
    private String outJobResultQueue;
    @Value("${rabbitmq.out.routingKey.jobResult}")
    private String outJobResultRoutingKey;
    @Value("${rabbitmq.out.durable.jobResult}")
    private boolean outJobResultDurable;
    @Value("${rabbitmq.out.exchange.otp}")
    private String outOtpExchange;
    @Value("${rabbitmq.out.queue.otp}")
    private String outOtpQueue;
    @Value("${rabbitmq.out.routingKey.otp}")
    private String outOtpRoutingKey;
    @Value("${rabbitmq.out.durable.otp}")
    private boolean outOtpDurable;

    @Value("${rabbitmq.in.exchange.disable}")
    private String inDisableExchange;
    @Value("${rabbitmq.in.queue.disable}")
    private String inDisableQueue;
    @Value("${rabbitmq.in.routingKey.disable}")
    private String inDisableRoutingKey;
    @Value("${rabbitmq.in.durable.disable}")
    private boolean inDisableDurable;
    @Value("${rabbitmq.out.exchange.disableResult}")
    private String outDisableResultExchange;
    @Value("${rabbitmq.out.queue.disableResult}")
    private String outDisableResultQueue;
    @Value("${rabbitmq.out.routingKey.disableResult}")
    private String outDisableResultRoutingKey;
    @Value("${rabbitmq.out.durable.disableResult}")
    private boolean outDisableResultDurable;

    @Value("${rabbitmq.in.exchange.resend}")
    private String inResendExchange;
    @Value("${rabbitmq.in.queue.resend}")
    private String inResendQueue;
    @Value("${rabbitmq.in.routingKey.resend}")
    private String inResendRoutingKey;
    @Value("${rabbitmq.in.durable.resend}")
    private boolean inResendDurable;
    @Value("${rabbitmq.out.exchange.activate}")
    private String outActivateExchange;
    @Value("${rabbitmq.out.queue.activate}")
    private String outActivateQueue;
    @Value("${rabbitmq.out.routingKey.activate}")
    private String outActivateRoutingKey;
    @Value("${rabbitmq.out.durable.activate}")
    private boolean outActivateDurable;
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
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue storeQueue() {
        return new Queue(storeQueue, storeDurable);
    }

    @Bean
    public DirectExchange storeExchange() {
        return new DirectExchange(storeExchange);
    }

    @Bean
    public Binding storeBinding() {
        return BindingBuilder.
                bind(storeQueue())
                .to(storeExchange())
                .with(storeRoutingKey);
    }

    @Bean
    public Queue inPublishQueue() {
        return new Queue(inPublishQueue, inPublishDurable);
    }

    @Bean
    public DirectExchange inPublishExchange() {
        return new DirectExchange(inPublishExchange);
    }

    @Bean
    public Binding inPublishBinding() {
        return BindingBuilder.
                bind(inPublishQueue())
                .to(inPublishExchange())
                .with(inPublishRoutingKey);
    }

    @Bean
    public Queue outPublishQueue() {
        return new Queue(outPublishQueue, outPublishDurable);
    }

    @Bean
    public DirectExchange outPublishExchange() {
        return new DirectExchange(outPublishExchange);
    }

    @Bean
    public Binding outPublishBinding() {
        return BindingBuilder.
                bind(outPublishQueue())
                .to(outPublishExchange())
                .with(outPublishRoutingKey);
    }

    @Bean
    public Queue outResultQueue() {
        return new Queue(outResultQueue, outResultDurable);
    }

    @Bean
    public DirectExchange outResultExchange() {
        return new DirectExchange(outResultExchange);
    }

    @Bean
    public Binding outResultBinding() {
        return BindingBuilder.
                bind(outResultQueue())
                .to(outResultExchange())
                .with(outResultRoutingKey);
    }

    @Bean
    public Queue outJobResultQueue() {
        return new Queue(outJobResultQueue, outJobResultDurable);
    }

    @Bean
    public DirectExchange outJobResultExchange() {
        return new DirectExchange(outJobResultExchange);
    }

    @Bean
    public Binding outJobResultBinding() {
        return BindingBuilder.
                bind(outJobResultQueue())
                .to(outJobResultExchange())
                .with(outJobResultRoutingKey);
    }

    @Bean
    public Queue outSmsOtpQueue() {
        return new Queue(outOtpQueue, outOtpDurable);
    }

    @Bean
    public DirectExchange outSmsOtpExchange() {
        return new DirectExchange(outOtpExchange);
    }

    @Bean
    public Binding outSmsOtptBinding() {
        return BindingBuilder.
                bind(outSmsOtpQueue())
                .to(outSmsOtpExchange())
                .with(outOtpRoutingKey);
    }

    @Bean
    public Queue inDisableQueue() {
        return new Queue(inDisableQueue, inDisableDurable);
    }

    @Bean
    public DirectExchange inDisableExchange() {
        return new DirectExchange(inDisableExchange);
    }

    @Bean
    public Binding inDisbletBinding() {
        return BindingBuilder.
                bind(inDisableQueue())
                .to(inDisableExchange())
                .with(inDisableRoutingKey);
    }

    @Bean
    public Queue outDisableResultQueue() {
        return new Queue(outDisableResultQueue, outDisableResultDurable);
    }

    @Bean
    public DirectExchange outDisableResultExchange() {
        return new DirectExchange(outDisableResultExchange);
    }

    @Bean
    public Binding outDisableResultBinding() {
        return BindingBuilder
                .bind(outDisableResultQueue())
                .to(outDisableResultExchange())
                .with(outDisableResultRoutingKey);
    }

    @Bean
    public Queue inResendQueue() {
        return new Queue(inResendQueue, inResendDurable);
    }

    @Bean
    public DirectExchange inResendExchange() {
        return new DirectExchange(inResendExchange);
    }

    @Bean
    public Binding inResendBinding() {
        return BindingBuilder
                .bind(inResendQueue())
                .to(inResendExchange())
                .with(inResendRoutingKey);
    }
    @Bean
    public Queue outActivateQueue() {
        return new Queue(outActivateQueue, outActivateDurable);
    }

    @Bean
    public DirectExchange outActivateExchange() {
        return new DirectExchange(outActivateExchange);
    }

    @Bean
    public Binding outActivateBinding() {
        return BindingBuilder
                .bind(outActivateQueue())
                .to(outActivateExchange())
                .with(outActivateRoutingKey);
    }
}
