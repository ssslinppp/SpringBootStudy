package com.ssslinppp;

import com.ssslinppp.hello.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 官方示例：https://spring.io/guides/gs/messaging-rabbitmq/
 */
@SpringBootApplication
public class RabbitmqMessageApplication {
    public final static String queueName = "spring-boot";

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqMessageApplication.class, args);
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /**
     * AMQP producers don’t send messages directly to queues.
     * Instead, a message is sent to an exchange, which can go to a single queue, or fanout to multiple queues, emulating the concept of
     * JMS topics
     *
     * @return
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    /**
     * Spring AMQP requires that the Queue, the TopicExchange, and the Binding be declared as top level Spring beans in order to be set
     * up properly.
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


}
