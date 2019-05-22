package com.ssslinppp.rabbitmq.springamqp.tut4;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 */
public class Tut4Sender {
    private final String[] keys = {"orange", "black", "green"};

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private int index;
    private int count;

    /**
     * 向Direct Exchange中发送消息，并指定 routingKey
     */
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello to ");
        if (++this.index == 3) {
            this.index = 0;
        }
        String key = keys[this.index];
        builder.append(key).append(' ');
        builder.append(Integer.toString(++this.count));
        String message = builder.toString();

        template.convertAndSend(direct.getName(), key, message); // 发送到 Exchange

        System.out.println(" [x] Sent '" + message + "'");
    }
}
