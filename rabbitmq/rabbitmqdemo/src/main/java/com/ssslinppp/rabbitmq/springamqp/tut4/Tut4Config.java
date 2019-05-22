package com.ssslinppp.rabbitmq.springamqp.tut4;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description：使用Direct Exchange，完成消息路由<br/>
 * 官方代码： http://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html
 * <p>
 */
@Profile({"tut4", "routing"})
@Configuration
public class Tut4Config {
    /**
     * 架构图参见：
     * http://www.rabbitmq.com/img/tutorials/direct-exchange.png
     * http://www.rabbitmq.com/img/tutorials/direct-exchange-multiple.png
     * 官网：
     * http://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html
     *
     * @return
     */
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("tut.direct");
    }

    /**
     * 向Direct Exchange中发送消息，并指定 routingKey
     *
     * @return
     */
    @Profile("sender")
    @Bean
    public Tut4Sender sender() {
        return new Tut4Sender();
    }

    /**
     * 创建2个Queue，并指定不同的routingKey;
     * 创建2个消费者，分别消费两个Queue;
     */
    @Profile("receiver")
    private static class ReceiverConfig {

        /**
         * Queue1：绑定orange和black
         *
         * @return
         */
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1a(DirectExchange direct,
                                 Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(direct).with("orange");
        }

        @Bean
        public Binding binding1b(DirectExchange direct,
                                 Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(direct).with("black");
        }

        /**
         * Queue2：绑定green和black
         *
         * @return
         */
        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }


        @Bean
        public Binding binding2a(DirectExchange direct,
                                 Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(direct).with("green");
        }

        @Bean
        public Binding binding2b(DirectExchange direct,
                                 Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(direct).with("black");
        }

        /**
         * 创建两个消费者，分别消费queue1 和 queue2
         *
         * @return
         */
        @Bean
        public Tut4Receiver receiver() {
            return new Tut4Receiver();
        }
    }

}
