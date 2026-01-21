package asia.castis.evoucher.push.config;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {
    @Value("${queue.in.name}")
    private String inQueue;
    @Value("${queue.out.name}")
    private String outQueue;
    @Value("${queue.dlq.name}")
    private String dlqQueue;
    @Value("${queue.in.exchange}")
    private String inExchange;
    @Value("${queue.dlq.exchange}")
    private String dlqExchange;
    @Value("${queue.out.exchange}")
    private String outExchange;
    @Value("${queue.in.routingKey}")
    private String inRoutingKey;
    @Value("${queue.out.routingKey}")
    private String outRoutingKey;
    @Value("${queue.dlq.routeKey}")
    private String dlqRoutingKey;
    @Value("${queue.in.duable}")
    private boolean inQueueDuable;
    @Value("${queue.out.duable}")
    private boolean outQueueDuable;

    /*
    @Bean
    public Queue inQueue(){
        return new Queue(inQueue, inQueueDuable);
    }
    */

    /*
    public Queue inQueue(){
        return QueueBuilder.durable(inQueue).build();
    }
    */

    // spring bean for queue (store json messages)
    @Bean
    public Queue outQueue(){
        //return new Queue(outQueue, outQueueDuable);
        return QueueBuilder.durable(outQueue).build();
    }
    @Bean
    public Queue inQueue(){
        //return new Queue(inQueue, inQueueDuable);
        return QueueBuilder.durable(inQueue)
                //.withArgument("x-dead-letter-exchange", dlqExchange)
                //.withArgument("x-dead-letter-routing-key",  dlqRoutingKey)
                .build();
    }
    /*
    @Bean
    public Queue dlq(){
        //return new Queue(dlqQueue, true);
        return QueueBuilder.durable(dlqQueue).build();
    }

     */
    @Bean
    public DirectExchange inExchange() {
        return new DirectExchange(inExchange);
    }
    /*
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlqExchange);
    }

     */
    @Bean
    public DirectExchange outExchange() {
        return new DirectExchange(outExchange);
    }

    @Bean
    public Binding inBinding() {
        return BindingBuilder.bind(inQueue()).to(inExchange()).with(inRoutingKey);
    }
    /*
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(dlqRoutingKey);
    }
    */
    @Bean
    public Binding outBinding() {
        return BindingBuilder.bind(outQueue()).to(outExchange()).with(outRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
