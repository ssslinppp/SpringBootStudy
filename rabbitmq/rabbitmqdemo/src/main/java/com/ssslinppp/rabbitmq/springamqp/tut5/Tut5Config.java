package com.ssslinppp.rabbitmq.springamqp.tut5;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/26 <br/>
 * Time: 17:52 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Profile({"tut5", "topics"})
@Configuration
public class Tut5Config {
    /**
     * 消息队列结构图： https://www.rabbitmq.com/img/tutorials/python-five.png
     * 官方示例：https://www.rabbitmq.com/tutorials/tutorial-five-spring-amqp.html
     * <p>
     * routingKey格式：用于描述动物, {@code <speed>.<colour>.<species>}
     *
     * @return
     */
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("tut.topic");
    }

    @Profile("sender")
    @Bean
    public Tut5Sender sender() {
        return new Tut5Sender();
    }

    /**
     * 创建了2个Queue：
     * <pre>
     *     Queue1: bingingKey： {@code "*.orange.*" }
     *     Queue2: bingingKey： {@code "*.*.rabbit" 和 "lazy.#"}
     * </pre>
     *
     * A message with a routing key set to :
     * <pre>
     * 1. "quick.orange.rabbit": will be delivered to both queues.
     * 2. "lazy.orange.elephant" : also will go to both of them.
     * 3. "quick.orange.fox" ： will only go to the first queue.
     * 4. "lazy.brown.fox" : only to the second.
     * 5. "lazy.pink.rabbit" : will be delivered to the second queue only once, even though it matches two bindings.
     * 6. "quick.brown.fox": doesn't match any binding so it will be discarded.
     * 7. "orange" or "quick.orange.male.rabbit":  these messages won't match any bindings and will be lost.
     * 8. "lazy.orange.male.rabbit":  even though it has four words, will match the last binding("lazy.#") and will be delivered to the second queue
     * </pre>
     */
    @Profile("receiver")
    private static class ReceiverConfig {
        /**
         * Queue1: bingingKey： {@code "*.orange.*" }
         * <p>
         * Queue1 is interested in all the orange animals.
         *
         * @return
         */
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1a(TopicExchange topic,
                                 Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.orange.*");
        }

        /**
         * Queue2: bingingKey： {@code "*.*.rabbit" 和 "lazy.#"}
         * <p>
         * Queue2 wants to hear everything about rabbits, and everything about lazy animals.
         *
         * @return
         */
        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding2a(TopicExchange topic,
                                 Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("lazy.#");
        }

        @Bean
        public Binding binding1b(TopicExchange topic,
                                 Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("*.*.rabbit");
        }

        @Bean
        public Tut5Receiver receiver() {
            return new Tut5Receiver();
        }

    }
}
