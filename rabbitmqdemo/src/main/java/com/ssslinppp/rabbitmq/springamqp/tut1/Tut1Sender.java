package com.ssslinppp.rabbitmq.springamqp.tut1;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/25 <br/>
 * Time: 16:17 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class Tut1Sender {
    @Autowired
    private RabbitTemplate template;

    /**
     * create in Tut1Config.java
     */
    @Autowired
    private Queue queue;

    /**
     * 定时任务，每1s发送一次消息
     */
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Hello World!";
        this.template.convertAndSend(queue.getName(), message);
        System.out.println(" [Send] Sent '" + message + "'");
    }
}
