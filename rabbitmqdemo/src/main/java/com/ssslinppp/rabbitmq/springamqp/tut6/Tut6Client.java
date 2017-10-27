package com.ssslinppp.rabbitmq.springamqp.tut6;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/27 <br/>
 * Time: 13:01 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class Tut6Client {
    int start = 0;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        System.out.println(" [x] Requesting fib(" + start + ")");
        Integer response = (Integer) template.convertSendAndReceive(exchange.getName(), "rpc", start++);
        System.out.println(" [.] Got '" + response + "'");
    }
}
