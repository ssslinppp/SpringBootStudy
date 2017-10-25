package com.ssslinppp.rabbitmq.springamqp.tut2;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description：WorkQueues: 单生产者-多消费者<br/>
 * User: liulin <br/>
 * Date: 2017/10/25 <br/>
 * Time: 17:35 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
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
