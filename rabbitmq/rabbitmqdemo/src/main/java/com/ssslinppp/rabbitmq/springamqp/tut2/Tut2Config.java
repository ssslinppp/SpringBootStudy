package com.ssslinppp.rabbitmq.springamqp.tut2;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description：WorkQueues: 单生产者-多消费者<br/>
 * https://www.rabbitmq.com/tutorials/tutorial-two-spring-amqp.html
 * <p>
 */
@Profile({"tut2", "work-queues"})
@Configuration
public class Tut2Config {
    @Bean
    public Queue hello() {
        return new Queue("hello-workqueuqe");
    }

    @Profile("sender")
    @Bean
    public Tut2Sender sender() {
        return new Tut2Sender();
    }

    /**
     * 多个消费者
     */
    @Profile("receiver")
    private static class ReceiverConfig {

        @Bean
        public Tut2Receiver receiver1() {
            return new Tut2Receiver(1);
        }

        @Bean
        public Tut2Receiver receiver2() {
            return new Tut2Receiver(2);
        }
    }
}
