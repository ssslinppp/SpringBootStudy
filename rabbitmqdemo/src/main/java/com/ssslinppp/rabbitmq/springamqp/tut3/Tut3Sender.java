package com.ssslinppp.rabbitmq.springamqp.tut3;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/26 <br/>
 * Time: 14:51 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class Tut3Sender {
    int dots = 0;
    int count = 0;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanout;

    /**
     * The messages will be lost if no queue is bound to the exchange yet, but that's okay for us;
     * if no consumer is listening yet we can safely discard the message.
     */
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        if (dots++ == 3) {
            dots = 1;
        }
        for (int i = 0; i < dots; i++) {
            builder.append('.');
        }
        builder.append(Integer.toString(++count));
        String message = builder.toString();
        template.convertAndSend(fanout.getName(), "", message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
