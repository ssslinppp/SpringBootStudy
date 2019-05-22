package com.ssslinppp.rabbitmq.springamqp.tut3;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description：发布/订阅模式<br/>
 * https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html
 * <p>
 */
@Profile({"tut3", "pub-sub", "publish-subscribe"})
@Configuration
public class Tut3Config {
    /**
     * FanoutExchange: 将接收到的message广播到所有Queue
     *
     * @return
     */
    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("tut.fanout");
    }


    @Profile("sender")
    @Bean
    public Tut3Sender sender() {
        return new Tut3Sender();
    }

    /**
     * 启动receiver程序时，自动创建2个匿名的Queue和2个bingings，并使用bingings将[Exchange, Queue] 绑定；
     * 自动创建2个消费者，并分别消费2个匿名的Queue；
     * <p>
     * <p>
     * Firstly, whenever we connect to Rabbit we need a fresh, empty queue.
     * To do this we could create a queue with a random name, or, even better - let the server choose a random queue name for us.
     * <p>
     * Secondly, once we disconnect the consumer the queue should be automatically deleted.
     * To do this with the spring-amqp client, we defined and AnonymousQueue, which creates a non-durable, exclusive, autodelete queue
     * with a generated name.
     *
     * @return
     */
    @Profile("receiver")
    private static class ReceiverConfig {

        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(FanoutExchange fanout,
                                Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
        }

        @Bean
        public Binding binding2(FanoutExchange fanout,
                                Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
        }

        @Bean
        public Tut3Receiver receiver() {
            return new Tut3Receiver();
        }
    }
}
